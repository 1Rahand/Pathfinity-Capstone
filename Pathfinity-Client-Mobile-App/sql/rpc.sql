create function rpc_delete_account() returns void
    security definer
    language plpgsql
as
$$
BEGIN
    IF is_user_admin() THEN
        RAISE EXCEPTION 'Cannot delete an admin account';
    END IF;

    DELETE FROM auth.users WHERE id = auth.uid();
END;
$$;