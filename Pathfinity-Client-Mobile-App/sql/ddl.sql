create table public.user_admins
(
    id             uuid primary key not null,
    email          text             not null,
    username       text             not null,
    full_name      text             not null,
    is_super_admin boolean                  default false,
    created_at     timestamp with time zone default now(),
    updated_at     timestamp with time zone default now(),
    foreign key (id) references auth.users (id) on update cascade on delete cascade
);

CREATE POLICY "allow all ops for superadmin" ON public.user_admins
    FOR ALL
    TO authenticated
    USING (is_super_admin)
    with check (is_super_admin);

-- admin can select their own account
CREATE POLICY "allow select for admin" ON public.user_admins
    FOR SELECT
    TO authenticated
    USING (auth.uid() = id)
;

create table public.user_alumni
(
    id          uuid primary key not null,
    first_name  text             not null,
    last_name   text             not null,
    birthdate   date             not null,
    email       text             not null,
    is_approved boolean                  default false,
    created_at  timestamp with time zone default now(),
    updated_at  timestamp with time zone default now(),
    foreign key (id) references auth.users (id) on update cascade on delete cascade
);

-- alumni can select their own account
CREATE POLICY "allow select for alumni" ON public.user_alumni
    FOR SELECT
    TO authenticated
    USING (auth.uid() = id);

-- allow all ops for admin
CREATE POLICY "allow all ops for admin" ON public.user_alumni
    FOR ALL
    TO authenticated
    USING (is_user_admin())
    with check (is_user_admin());

CREATE OR REPLACE FUNCTION public.is_user_admin()
    RETURNS boolean
    LANGUAGE sql
    SECURITY DEFINER
AS $$
SELECT EXISTS (
    SELECT 1
    FROM public.user_admins
    WHERE id = auth.uid()
);
$$;


create table public.user_companies
(
    id           uuid primary key not null,
    company_name text             not null,
    email        text             not null,
    is_approved  boolean                  default false,
    created_at   timestamp with time zone default now(),
    updated_at   timestamp with time zone default now(),
    foreign key (id) references auth.users (id) on update cascade on delete cascade
);


create table public.user_content_creators
(
    id          uuid primary key not null,
    first_name  text             not null,
    last_name   text             not null,
    birthdate   date             not null,
    email       text             not null,
    is_approved boolean                  default false,
    created_at  timestamp with time zone default now(),
    updated_at  timestamp with time zone default now(),
    foreign key (id) references auth.users (id) on update cascade on delete cascade
);

create table public.user_students
(
    id                 uuid primary key         not null default auth.uid(),
    first_name         text                     not null,
    last_name          text                     not null,
    birthdate          date                     not null,
    premium            boolean                  not null default false,
    premium_expires_at timestamp with time zone null,
    created_at         timestamp with time zone not null default now(),
    updated_at        timestamp with time zone not null default now(),
    active             boolean                  not null default true,
    gender             text                     not null,
    foreign key (id) references auth.users (id) on update cascade on delete cascade
);


-- students can be selected by anyone
CREATE POLICY "allow all ops for all" ON storage.objects
    FOR all
    TO authenticated
    USING (true)
with check (true);
-- students do all ops on their own account
CREATE POLICY "allow all ops for student" ON public.user_students
    FOR ALL
    TO authenticated
    USING (auth.uid() = id)
    with check (auth.uid() = id);

-- allow all ops for admin
CREATE POLICY "allow all ops for admin" ON public.user_students
    FOR ALL
    TO authenticated
    USING (is_user_admin())
    with check (is_user_admin());

-- Content

drop table if exists public.courses cascade ;
create table public.courses
(
    id   uuid primary key not null default gen_random_uuid(),
    title text            not null default ''::text,
    description text            not null default ''::text,
    updated_at timestamp with time zone default now(),
    created_at timestamp with time zone default now(),
    creator_id uuid             not null ,
    is_approved boolean                  default false,
    is_active boolean                   default true,
    rejection_reason text,
    foreign key (creator_id) references public.user_content_creators (id) on update cascade on delete cascade
);

alter table courses add column description text default null;

CREATE TABLE IF NOT EXISTS course_videos (
  id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
  title TEXT NOT NULL,
  description TEXT NOT NULL,
  video_url TEXT NOT NULL,
  sequence_number INTEGER NOT NULL,
  created_at timestamp with time zone NOT NULL DEFAULT NOW(),
  updated_at timestamp with time zone NOT NULL DEFAULT NOW()
);

-- allow all ops for admin
CREATE POLICY "allow all ops for admin" ON public.course_videos
    FOR ALL
    TO authenticated
    USING (is_user_admin())
    with check (is_user_admin());

-- allow all ops for content creator
CREATE POLICY "allow all ops for content creator" ON public.course_videos
    FOR ALL
    TO authenticated
    USING (course_id IN (SELECT id FROM public.courses WHERE creator_id = auth.uid()))
    with check (course_id IN (SELECT id FROM public.courses WHERE creator_id = auth.uid()));

-- allow select for all
CREATE POLICY "allow select for all" ON public.course_videos
    FOR SELECT
    TO public
    USING (true);


create table public.internships
(
    id          uuid primary key not null default gen_random_uuid(),
    company_id  uuid             not null,
    title       text             not null,
    description text             not null,
    duration    text             not null,
    skills      text[]           not null,
    is_approved boolean                   default false,
    created_at  timestamp with time zone  default now(),
    updated_at  timestamp with time zone  default now(),
    foreign key (company_id) references public.user_companies (id) on update cascade on delete cascade
);

-- any one can see internships
CREATE POLICY "allow select for all" ON public.internships
    FOR SELECT
    TO public
    USING (true);

-- allow all ops for admin
CREATE POLICY "allow all ops for admin" ON public.internships
    FOR ALL
    TO authenticated
    USING (is_user_admin())
    with check (is_user_admin());
-- allow all ops for company
CREATE POLICY "allow all ops for company" ON public.internships
    FOR ALL
    TO authenticated
    USING (company_id = auth.uid())
    with check (company_id = auth.uid());

CREATE TABLE IF NOT EXISTS course_changes (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID REFERENCES courses(id) ON DELETE CASCADE,
  title TEXT,
  description TEXT,
  is_reviewed BOOLEAN DEFAULT false,
  is_approved BOOLEAN,
  rejection_reason TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS video_changes (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_video_id UUID REFERENCES course_videos(id) ON DELETE CASCADE,
  title TEXT,
  description TEXT,
  video_url TEXT,
  is_reviewed BOOLEAN DEFAULT false,
  is_approved BOOLEAN,
  rejection_reason TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- rls
CREATE POLICY "course changes can be selected by everyone"
    ON public.course_changes
    FOR SELECT
    TO public
    USING (true);

CREATE POLICY "allow all ops for admin" ON public.course_changes
    FOR ALL
    TO authenticated
    USING (is_user_admin())
    with check (is_user_admin());

CREATE POLICY "allow all ops for content creator" ON public.course_changes
    FOR ALL
    TO authenticated
    USING (course_id IN (SELECT id FROM public.courses WHERE creator_id = auth.uid()))
    with check (course_id IN (SELECT id FROM public.courses WHERE creator_id = auth.uid()));

-- video
CREATE POLICY "video changes can be selected by everyone"
    ON public.video_changes
    FOR SELECT
    TO public
    USING (true);

CREATE POLICY "allow all ops for admin" ON public.video_changes
    FOR ALL
    TO authenticated
    USING (is_user_admin())
    with check (is_user_admin());

CREATE POLICY "allow all ops for content creator" ON public.video_changes
    FOR ALL
    TO authenticated
    USING (course_video_id IN (SELECT id FROM public.course_videos WHERE course_id IN (SELECT id FROM public.courses WHERE creator_id = auth.uid())))
    with check (course_video_id IN (SELECT id FROM public.course_videos WHERE course_id IN (SELECT id FROM public.courses WHERE creator_id = auth.uid())));

-- content_creator rls
CREATE POLICY "allow all ops for content creator" on public.user_content_creators
    FOR ALL
    TO authenticated
    USING (auth.uid() = id)
    with check (auth.uid() = id);

-- select for all
CREATE POLICY "allow select for all" on public.user_content_creators
    FOR SELECT
    TO public
    USING (true);

-- allow all ops for admin
CREATE POLICY "allow all ops for admin" on public.user_content_creators
    FOR ALL
    TO authenticated
    USING (is_user_admin())
    with check (is_user_admin());

-- all ops for company
CREATE POLICY "allow all ops for company" on public.user_companies
    FOR ALL
    TO authenticated
    USING (auth.uid() = id)
    with check (auth.uid() = id);

-- select for all
CREATE POLICY "allow select for all" on public.user_companies
    FOR SELECT
    TO public
    USING (true);

-- allow all ops for admin
CREATE POLICY "allow all ops for admin" on public.user_companies
    FOR ALL
    TO authenticated
    USING (is_user_admin())
    with check (is_user_admin());

-- Enable RLS on all tables
ALTER TABLE public.user_admins ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.user_alumni ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.user_companies ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.user_content_creators ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.user_students ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.courses ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.course_videos ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.internships ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.course_changes ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.video_changes ENABLE ROW LEVEL SECURITY;

