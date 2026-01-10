-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create courses table
CREATE TABLE IF NOT EXISTS courses (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  creator_id UUID NOT NULL REFERENCES user_content_creator(id) ON DELETE CASCADE,
  title TEXT NOT NULL,
  description TEXT NOT NULL,
  is_approved BOOLEAN,
  is_active BOOLEAN DEFAULT true,
  rejection_reason TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Create course_videos table
CREATE TABLE IF NOT EXISTS course_videos (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
  title TEXT NOT NULL,
  description TEXT NOT NULL,
  video_url TEXT NOT NULL,
  sequence_number INTEGER NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Create trigger for courses updated_at
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger 
    WHERE tgname = 'update_courses_updated_at'
  ) THEN
    CREATE TRIGGER update_courses_updated_at
      BEFORE UPDATE ON courses
      FOR EACH ROW
      EXECUTE FUNCTION update_updated_at_column();
  END IF;
END;
$$;

-- Create trigger for course_videos updated_at
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger 
    WHERE tgname = 'update_course_videos_updated_at'
  ) THEN
    CREATE TRIGGER update_course_videos_updated_at
      BEFORE UPDATE ON course_videos
      FOR EACH ROW
      EXECUTE FUNCTION update_updated_at_column();
  END IF;
END;
$$;

-- Enable RLS on new tables
ALTER TABLE courses ENABLE ROW LEVEL SECURITY;
ALTER TABLE course_videos ENABLE ROW LEVEL SECURITY;

-- Courses RLS policies
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Content creators can view their own courses'
  ) THEN
    CREATE POLICY "Content creators can view their own courses"
      ON courses
      FOR SELECT
      USING (creator_id = auth.uid());
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Admins can view all courses'
  ) THEN
    CREATE POLICY "Admins can view all courses"
      ON courses
      FOR SELECT
      USING (EXISTS (SELECT 1 FROM user_admins WHERE id = auth.uid()));
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Content creators can create courses'
  ) THEN
    CREATE POLICY "Content creators can create courses"
      ON courses
      FOR INSERT
      WITH CHECK (creator_id = auth.uid());
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Content creators can update their own unapproved courses'
  ) THEN
    CREATE POLICY "Content creators can update their own unapproved courses"
      ON courses
      FOR UPDATE
      USING (creator_id = auth.uid() AND is_approved IS NULL);
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Admins can update any course'
  ) THEN
    CREATE POLICY "Admins can update any course"
      ON courses
      FOR UPDATE
      USING (EXISTS (SELECT 1 FROM user_admins WHERE id = auth.uid()));
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Content creators can delete their own unapproved courses'
  ) THEN
    CREATE POLICY "Content creators can delete their own unapproved courses"
      ON courses
      FOR DELETE
      USING (creator_id = auth.uid() AND is_approved IS NULL);
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Admins can delete any course'
  ) THEN
    CREATE POLICY "Admins can delete any course"
      ON courses
      FOR DELETE
      USING (EXISTS (SELECT 1 FROM user_admins WHERE id = auth.uid()));
  END IF;
END;
$$;

-- Course videos RLS policies
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Content creators can view videos of their courses'
  ) THEN
    CREATE POLICY "Content creators can view videos of their courses"
      ON course_videos
      FOR SELECT
      USING (EXISTS (
        SELECT 1 FROM courses 
        WHERE id = course_id 
        AND creator_id = auth.uid()
      ));
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Admins can view all course videos'
  ) THEN
    CREATE POLICY "Admins can view all course videos"
      ON course_videos
      FOR SELECT
      USING (EXISTS (SELECT 1 FROM user_admins WHERE id = auth.uid()));
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Content creators can add videos to their unapproved courses'
  ) THEN
    CREATE POLICY "Content creators can add videos to their unapproved courses"
      ON course_videos
      FOR INSERT
      WITH CHECK (EXISTS (
        SELECT 1 FROM courses 
        WHERE id = course_id 
        AND creator_id = auth.uid()
        AND is_approved IS NULL
      ));
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Content creators can update videos of their unapproved courses'
  ) THEN
    CREATE POLICY "Content creators can update videos of their unapproved courses"
      ON course_videos
      FOR UPDATE
      USING (EXISTS (
        SELECT 1 FROM courses 
        WHERE id = course_id 
        AND creator_id = auth.uid()
        AND is_approved IS NULL
      ));
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Admins can update any course video'
  ) THEN
    CREATE POLICY "Admins can update any course video"
      ON course_videos
      FOR UPDATE
      USING (EXISTS (SELECT 1 FROM user_admins WHERE id = auth.uid()));
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Content creators can delete videos from their unapproved courses'
  ) THEN
    CREATE POLICY "Content creators can delete videos from their unapproved courses"
      ON course_videos
      FOR DELETE
      USING (EXISTS (
        SELECT 1 FROM courses 
        WHERE id = course_id 
        AND creator_id = auth.uid()
        AND is_approved IS NULL
      ));
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Admins can delete any course video'
  ) THEN
    CREATE POLICY "Admins can delete any course video"
      ON course_videos
      FOR DELETE
      USING (EXISTS (SELECT 1 FROM user_admins WHERE id = auth.uid()));
  END IF;
END;
$$;

-- Helper functions
CREATE OR REPLACE FUNCTION approve_course(p_course_id UUID)
RETURNS VOID AS $$
BEGIN
  UPDATE courses 
  SET is_approved = TRUE, rejection_reason = NULL
  WHERE id = p_course_id;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE OR REPLACE FUNCTION reject_course(p_course_id UUID, p_rejection_reason TEXT)
RETURNS VOID AS $$
BEGIN
  UPDATE courses 
  SET is_approved = FALSE, rejection_reason = p_rejection_reason
  WHERE id = p_course_id;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Create storage bucket for course videos
INSERT INTO storage.buckets (id, name, public)
SELECT 'course-videos', 'course-videos', false
WHERE NOT EXISTS (
  SELECT 1 FROM storage.buckets WHERE id = 'course-videos'
);

-- Storage policies for course-videos bucket
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Content creators can upload videos'
  ) THEN
    CREATE POLICY "Content creators can upload videos"
      ON storage.objects
      FOR INSERT
      WITH CHECK (
        bucket_id = 'course-videos' 
        AND EXISTS (
          SELECT 1 FROM user_content_creator 
          WHERE id = auth.uid() 
          AND is_approved = true
        )
      );
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Content creators can access their course videos'
  ) THEN
    CREATE POLICY "Content creators can access their course videos"
      ON storage.objects
      FOR SELECT
      USING (
        bucket_id = 'course-videos' 
        AND EXISTS (
          SELECT 1 FROM courses c 
          JOIN course_videos cv ON cv.course_id = c.id 
          WHERE c.creator_id = auth.uid() 
          AND cv.video_url = name
        )
      );
  END IF;
END;
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies 
    WHERE policyname = 'Admins have full access to course videos'
  ) THEN
    CREATE POLICY "Admins have full access to course videos"
      ON storage.objects
      FOR ALL
      USING (
        bucket_id = 'course-videos' 
        AND EXISTS (
          SELECT 1 FROM user_admins 
          WHERE id = auth.uid()
        )
      );
  END IF;
END;
$$;
