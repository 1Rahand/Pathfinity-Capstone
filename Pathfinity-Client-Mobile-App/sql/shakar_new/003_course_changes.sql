-- Create tables for tracking course and video changes
CREATE TABLE IF NOT EXISTS course_changes (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
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
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
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

-- Create triggers for updated_at
CREATE TRIGGER update_course_changes_updated_at
  BEFORE UPDATE ON course_changes
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_video_changes_updated_at
  BEFORE UPDATE ON video_changes
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

-- Enable RLS
ALTER TABLE course_changes ENABLE ROW LEVEL SECURITY;
ALTER TABLE video_changes ENABLE ROW LEVEL SECURITY;

-- RLS Policies for course_changes
CREATE POLICY "Content creators can view their course changes"
  ON course_changes
  FOR SELECT
  USING (EXISTS (
    SELECT 1 FROM courses 
    WHERE id = course_id 
    AND creator_id = auth.uid()
  ));

CREATE POLICY "Admins can view all course changes"
  ON course_changes
  FOR SELECT
  USING (EXISTS (
    SELECT 1 FROM user_admins 
    WHERE id = auth.uid()
  ));

CREATE POLICY "Content creators can create course changes"
  ON course_changes
  FOR INSERT
  WITH CHECK (EXISTS (
    SELECT 1 FROM courses 
    WHERE id = course_id 
    AND creator_id = auth.uid()
  ));

CREATE POLICY "Content creators can update their unreviewed changes"
  ON course_changes
  FOR UPDATE
  USING (
    EXISTS (
      SELECT 1 FROM courses 
      WHERE id = course_id 
      AND creator_id = auth.uid()
    )
    AND is_reviewed = false
  );

CREATE POLICY "Admins can update course changes"
  ON course_changes
  FOR UPDATE
  USING (EXISTS (
    SELECT 1 FROM user_admins 
    WHERE id = auth.uid()
  ));

-- RLS Policies for video_changes
CREATE POLICY "Content creators can view their video changes"
  ON video_changes
  FOR SELECT
  USING (EXISTS (
    SELECT 1 FROM courses c
    JOIN course_videos cv ON cv.course_id = c.id
    WHERE cv.id = course_video_id 
    AND c.creator_id = auth.uid()
  ));

CREATE POLICY "Admins can view all video changes"
  ON video_changes
  FOR SELECT
  USING (EXISTS (
    SELECT 1 FROM user_admins 
    WHERE id = auth.uid()
  ));

CREATE POLICY "Content creators can create video changes"
  ON video_changes
  FOR INSERT
  WITH CHECK (EXISTS (
    SELECT 1 FROM courses c
    JOIN course_videos cv ON cv.course_id = c.id
    WHERE cv.id = course_video_id 
    AND c.creator_id = auth.uid()
  ));

CREATE POLICY "Content creators can update their unreviewed changes"
  ON video_changes
  FOR UPDATE
  USING (
    EXISTS (
      SELECT 1 FROM courses c
      JOIN course_videos cv ON cv.course_id = c.id
      WHERE cv.id = course_video_id 
      AND c.creator_id = auth.uid()
    )
    AND is_reviewed = false
  );

CREATE POLICY "Admins can update video changes"
  ON video_changes
  FOR UPDATE
  USING (EXISTS (
    SELECT 1 FROM user_admins 
    WHERE id = auth.uid()
  ));

-- Helper functions for reviewing changes
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

CREATE OR REPLACE FUNCTION reject_course_changes(p_course_id UUID, p_rejection_reason TEXT)
RETURNS VOID AS $$
BEGIN
  -- Mark latest unreviewed change as reviewed and rejected
  UPDATE course_changes
  SET is_reviewed = true,
      is_approved = false,
      rejection_reason = p_rejection_reason
  WHERE course_id = p_course_id
  AND is_reviewed = false;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE OR REPLACE FUNCTION approve_video_changes(p_video_id UUID)
RETURNS VOID AS $$
DECLARE
  v_video_change video_changes;
BEGIN
  -- Get the latest unreviewed change
  SELECT * INTO v_video_change
  FROM video_changes
  WHERE course_video_id = p_video_id
  AND is_reviewed = false
  ORDER BY created_at DESC
  LIMIT 1;

  IF FOUND THEN
    -- Update the video with new values
    UPDATE course_videos
    SET title = v_video_change.title,
        description = v_video_change.description,
        video_url = COALESCE(v_video_change.video_url, video_url)
    WHERE id = p_video_id;

    -- Mark change as reviewed and approved
    UPDATE video_changes
    SET is_reviewed = true,
        is_approved = true
    WHERE id = v_video_change.id;
  END IF;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE OR REPLACE FUNCTION reject_video_changes(p_video_id UUID, p_rejection_reason TEXT)
RETURNS VOID AS $$
BEGIN
  -- Mark latest unreviewed change as reviewed and rejected
  UPDATE video_changes
  SET is_reviewed = true,
      is_approved = false,
      rejection_reason = p_rejection_reason
  WHERE course_video_id = p_video_id
  AND is_reviewed = false;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;
