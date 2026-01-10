-- Add review fields to course_videos table
ALTER TABLE course_videos
ADD COLUMN IF NOT EXISTS is_reviewed BOOLEAN DEFAULT false,
ADD COLUMN IF NOT EXISTS is_approved BOOLEAN,
ADD COLUMN IF NOT EXISTS rejection_reason TEXT;

-- Update course_videos policies
DROP POLICY IF EXISTS "Content creators can add videos to their unapproved courses" ON course_videos;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Content creators can add videos to their courses'
  ) THEN
    CREATE POLICY "Content creators can add videos to their courses"
      ON course_videos
      FOR INSERT
      WITH CHECK (EXISTS (
        SELECT 1 FROM courses 
        WHERE id = course_id 
        AND creator_id = auth.uid()
      ));
  END IF;
END;
$$;

-- Helper functions for reviewing videos
CREATE OR REPLACE FUNCTION approve_video(p_video_id UUID)
RETURNS VOID AS $$
BEGIN
  UPDATE course_videos 
  SET is_reviewed = true,
      is_approved = true,
      rejection_reason = NULL
  WHERE id = p_video_id;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE OR REPLACE FUNCTION reject_video(p_video_id UUID, p_rejection_reason TEXT)
RETURNS VOID AS $$
BEGIN
  UPDATE course_videos 
  SET is_reviewed = true,
      is_approved = false,
      rejection_reason = p_rejection_reason
  WHERE id = p_video_id;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;
