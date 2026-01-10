create table public.course_changes (
  id uuid primary key not null default gen_random_uuid(),
  course_id uuid,
  title text,
  description text,
  is_reviewed boolean default false,
  is_approved boolean,
  rejection_reason text,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now(),
  foreign key (course_id) references public.courses (id)
  match simple on update no action on delete cascade
);

create table public.course_videos (
  id uuid primary key not null default gen_random_uuid(),
  course_id uuid not null,
  title text not null,
  description text not null,
  video_url text not null,
  sequence_number integer not null,
  created_at timestamp with time zone not null default now(),
  updated_at timestamp with time zone not null default now(),
  foreign key (course_id) references public.courses (id)
  match simple on update no action on delete cascade
);

create table public.courses (
  id uuid primary key not null default gen_random_uuid(),
  title text not null default ''::text,
  updated_at timestamp with time zone default now(),
  created_at timestamp with time zone default now(),
  creator_id uuid not null,
  is_approved boolean default false,
  is_active boolean default true,
  rejection_reason text,
  foreign key (creator_id) references public.user_content_creators (id)
  match simple on update cascade on delete cascade
);

create table public.internships (
  id uuid primary key not null default gen_random_uuid(),
  company_id uuid not null,
  title text not null,
  description text not null,
  duration text not null,
  skills text[] not null,
  is_approved boolean default false,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now(),
  foreign key (company_id) references public.user_companies (id)
  match simple on update cascade on delete cascade
);

create table public.user_admins (
  id uuid primary key not null,
  email text not null,
  username text not null,
  first_name text not null,
  is_super_admin boolean default false,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now(),
  last_name text,
  foreign key (id) references auth.users (id)
  match simple on update cascade on delete cascade
);

create table public.user_alumni (
  id uuid primary key not null,
  first_name text not null,
  last_name text not null,
  birthdate date not null,
  email text not null,
  is_approved boolean default false,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now(),
  foreign key (id) references auth.users (id)
  match simple on update cascade on delete cascade
);

create table public.user_companies (
  id uuid primary key not null,
  company_name text not null,
  email text not null,
  is_approved boolean default false,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now(),
  foreign key (id) references auth.users (id)
  match simple on update cascade on delete cascade
);

create table public.user_content_creators (
  id uuid primary key not null,
  first_name text not null,
  last_name text not null,
  birthdate date not null,
  email text not null,
  is_approved boolean default false,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now(),
  foreign key (id) references auth.users (id)
  match simple on update cascade on delete cascade
);

create table public.user_students (
  id uuid primary key not null default auth.uid(),
  first_name text not null,
  last_name text not null,
  birthdate date not null,
  premium boolean not null default false,
  premium_expires_at timestamp with time zone,
  created_at timestamp with time zone not null default now(),
  active boolean not null default true,
  gender text not null,
  updated_at timestamp with time zone default now(),
  foreign key (id) references auth.users (id)
  match simple on update cascade on delete cascade
);

create table public.video_changes (
  id uuid primary key not null default gen_random_uuid(),
  course_video_id uuid,
  title text,
  description text,
  video_url text,
  is_reviewed boolean default false,
  is_approved boolean,
  rejection_reason text,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now(),
  foreign key (course_video_id) references public.course_videos (id)
  match simple on update no action on delete cascade
);



create function handle_new_user_safe() returns trigger
    security definer
    SET search_path = public
    language plpgsql
as
$$
DECLARE
    required_field text;
    missing_fields text[];
BEGIN
    -- Common validation
    IF NEW.raw_user_meta_data IS NULL OR NEW.raw_user_meta_data::text = '{}'::text THEN
        RAISE EXCEPTION 'User metadata cannot be empty';
    END IF;

    IF NEW.raw_user_meta_data ->> 'p_user_type' IS NULL THEN
        RAISE EXCEPTION 'User type (p_user_type) is required in metadata';
    END IF;

    -- Handle student user
    IF NEW.raw_user_meta_data ->> 'p_user_type' = 'user_student' THEN
        -- Validate required fields




        INSERT INTO user_students(id , email)
        VALUES (NEW.id , NEW.email);

    END IF;

    -- If no recognized user type was processed
    IF NEW.raw_user_meta_data ->> 'p_user_type' NOT IN ('user_student', 'user_alumni', 'user_company', 'user_content_creator', 'user_admin') THEN
        RAISE EXCEPTION 'Invalid user type: %', NEW.raw_user_meta_data ->> 'p_user_type';
    END IF;

    RETURN NEW;
EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION 'User creation failed: %', SQLERRM;
END;
$$;


drop trigger handle_new_user on auth.users;
create trigger handle_new_user
    after insert on auth.users
    for each row
    execute procedure handle_new_user_safe();