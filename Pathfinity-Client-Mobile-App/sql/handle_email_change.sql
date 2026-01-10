create function public.sync_profile_email() returns trigger
    security definer
    language plpgsql
as
$$
BEGIN
    IF OLD.email IS DISTINCT FROM NEW.email THEN
        UPDATE public.user_students
        SET email = NEW.email
        WHERE id = NEW.id;
    END IF;
    RETURN NEW;
END;
$$;

create trigger sync_user_email_to_profile
    after update
        of email
    on auth.users
    for each row
    execute procedure public.sync_profile_email();

