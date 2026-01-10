-- Function to get user type and approval status
DROP FUNCTION IF EXISTS get_user_type_and_status();
CREATE OR REPLACE FUNCTION get_user_type_and_status()
RETURNS TABLE (
  user_type TEXT,
  is_approved BOOLEAN,
  is_super_admin BOOLEAN
) AS $$
DECLARE
  user_id UUID := auth.uid();
BEGIN
  -- Check if user is an admin
  IF EXISTS (SELECT 1 FROM user_admins WHERE id = user_id) THEN
    RETURN QUERY SELECT
      'admin'::TEXT,
      TRUE::BOOLEAN,
      (SELECT user_admins.is_super_admin FROM user_admins WHERE user_admins.id = user_id);
  -- Check if user is a content creator
  ELSIF EXISTS (SELECT 1 FROM user_content_creators WHERE id = user_id) THEN
    RETURN QUERY SELECT
      'content_creator'::TEXT,
      (SELECT user_content_creators.is_approved FROM user_content_creators WHERE user_content_creators.id = user_id),
      FALSE::BOOLEAN;
  -- Check if user is an alumni
  ELSIF EXISTS (SELECT 1 FROM user_alumni WHERE id = user_id) THEN
    RETURN QUERY SELECT
      'alumni'::TEXT,
      (SELECT user_alumni.is_approved FROM user_alumni WHERE user_alumni.id = user_id),
      FALSE::BOOLEAN;
  -- Check if user is a company
  ELSIF EXISTS (SELECT 1 FROM user_companies WHERE id = user_id) THEN
    RETURN QUERY SELECT
      'company'::TEXT,
      (SELECT user_companies.is_approved FROM user_companies WHERE user_companies.id = user_id),
      FALSE::BOOLEAN;
  ELSE
    RETURN QUERY SELECT NULL::TEXT, NULL::BOOLEAN, NULL::BOOLEAN;
  END IF;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;


-- Function to approve course changes
DROP FUNCTION IF EXISTS approve_course_changes(UUID);
CREATE OR REPLACE FUNCTION approve_course_changes(p_course_id UUID)
RETURNS VOID AS $$
DECLARE
  v_course_change course_changes;
BEGIN
  -- Get the latest unreviewed change
  SELECT * INTO v_course_change
  FROM course_changes
  WHERE course_id = p_course_id
    AND is_reviewed = false
  ORDER BY created_at DESC
  LIMIT 1;

  IF FOUND THEN
    -- Update the course with new values
    UPDATE courses
    SET title = v_course_change.title,
        description = v_course_change.description
    WHERE id = p_course_id;

    -- Mark change as reviewed and approved
    UPDATE course_changes
    SET is_reviewed = true,
        is_approved = true
    WHERE id = v_course_change.id;
  END IF;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;