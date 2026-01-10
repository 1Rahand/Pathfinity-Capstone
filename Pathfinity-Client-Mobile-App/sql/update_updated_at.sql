create function update_updated_at() returns trigger
    language plpgsql
as
$$
BEGIN
    NEW.updated_at := NOW();  -- Set timestamp to the current timestamp
RETURN NEW;  -- Return the updated row
END;
$$;