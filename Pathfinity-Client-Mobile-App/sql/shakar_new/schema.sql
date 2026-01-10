-- Schema for Admin Panel with Supabase

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create tables for different user types
CREATE TABLE IF NOT EXISTS user_admins (
  id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  full_name TEXT NOT NULL,
  email TEXT, -- Added email field
  is_super_admin BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS user_content_creator (
  id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  full_name TEXT NOT NULL,
  email TEXT, -- Added email field
  is_approved BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS user_alumni (
  id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  full_name TEXT NOT NULL,
  email TEXT, -- Added email field
  is_approved BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS user_companies (
  id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  company_name TEXT NOT NULL,
  email TEXT, -- Added email field
  is_approved BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Drop existing function if it exists
DROP FUNCTION IF EXISTS update_updated_at_column();

-- Create function to automatically update updated_at timestamp
CREATE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers to automatically update updated_at timestamp
CREATE TRIGGER update_user_admins_updated_at
BEFORE UPDATE ON user_admins
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_user_content_creator_updated_at
BEFORE UPDATE ON user_content_creator
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_user_alumni_updated_at
BEFORE UPDATE ON user_alumni
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_user_companies_updated_at
BEFORE UPDATE ON user_companies
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Drop existing function if it exists
DROP FUNCTION IF EXISTS get_user_type_and_status();

-- Create function to get user type and approval status
CREATE FUNCTION get_user_type_and_status()
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
  ELSIF EXISTS (SELECT 1 FROM user_content_creator WHERE id = user_id) THEN
    RETURN QUERY SELECT 
      'content_creator'::TEXT, 
      (SELECT user_content_creator.is_approved FROM user_content_creator WHERE user_content_creator.id = user_id),
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

-- Drop existing function if it exists
DROP FUNCTION IF EXISTS approve_user(UUID, TEXT);

-- Create function to approve a user
CREATE FUNCTION approve_user(p_user_id UUID, p_user_type TEXT)
RETURNS VOID AS $$
BEGIN
  IF p_user_type = 'content_creator' THEN
    UPDATE user_content_creator SET is_approved = TRUE WHERE id = p_user_id;
  ELSIF p_user_type = 'alumni' THEN
    UPDATE user_alumni SET is_approved = TRUE WHERE id = p_user_id;
  ELSIF p_user_type = 'company' THEN
    UPDATE user_companies SET is_approved = TRUE WHERE id = p_user_id;
  ELSE
    RAISE EXCEPTION 'Invalid user type: %', p_user_type;
  END IF;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Drop existing function if it exists
DROP FUNCTION IF EXISTS create_admin(UUID, TEXT, TEXT, BOOLEAN);

-- Create function to create an admin user
CREATE FUNCTION create_admin(p_user_id UUID, p_full_name TEXT, p_email TEXT, p_is_super_admin BOOLEAN)
RETURNS VOID AS $$
BEGIN
  INSERT INTO user_admins (id, full_name, email, is_super_admin)
  VALUES (p_user_id, p_full_name, p_email, p_is_super_admin);
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Enable Row Level Security (RLS)
ALTER TABLE user_admins ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_content_creator ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_alumni ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_companies ENABLE ROW LEVEL SECURITY;

-- Drop existing policies if they exist
DROP POLICY IF EXISTS "Admins can view all admin profiles" ON user_admins;
DROP POLICY IF EXISTS "Super admins can insert admin profiles" ON user_admins;
DROP POLICY IF EXISTS "Super admins can update admin profiles" ON user_admins;
DROP POLICY IF EXISTS "Super admins can delete admin profiles" ON user_admins;

-- Create RLS policies for user_admins
CREATE POLICY "Admins can view all admin profiles"
ON user_admins
FOR SELECT
USING (TRUE);  -- Allow all authenticated users to view admin profiles

CREATE POLICY "Admins can view their own profile"
ON user_admins
FOR SELECT
USING (id = auth.uid());

CREATE POLICY "Super admins can insert admin profiles"
ON user_admins
FOR INSERT
WITH CHECK (
  (SELECT user_admins.is_super_admin FROM user_admins WHERE user_admins.id = auth.uid())
);

CREATE POLICY "Super admins can update admin profiles"
ON user_admins
FOR UPDATE
USING (
  (SELECT user_admins.is_super_admin FROM user_admins WHERE user_admins.id = auth.uid())
);

CREATE POLICY "Super admins can delete admin profiles"
ON user_admins
FOR DELETE
USING (
  (SELECT user_admins.is_super_admin FROM user_admins WHERE user_admins.id = auth.uid())
);

-- Allow initial admin creation
CREATE POLICY "Allow initial admin creation"
ON user_admins
FOR INSERT
WITH CHECK (
  NOT EXISTS (SELECT 1 FROM user_admins)  -- Only if no admins exist yet
);

-- Create RLS policies for user_content_creator
CREATE POLICY "Admins can view all content creator profiles"
ON user_content_creator
FOR SELECT
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

CREATE POLICY "Content creators can view their own profile"
ON user_content_creator
FOR SELECT
USING (
  id = auth.uid()
);

CREATE POLICY "Admins can insert content creator profiles"
ON user_content_creator
FOR INSERT
WITH CHECK (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

CREATE POLICY "Users can insert their own content creator profile during registration"
ON user_content_creator
FOR INSERT
WITH CHECK (
  id = auth.uid()
);

CREATE POLICY "Admins can update content creator profiles"
ON user_content_creator
FOR UPDATE
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

CREATE POLICY "Content creators can update their own profile"
ON user_content_creator
FOR UPDATE
USING (
  id = auth.uid()
);

CREATE POLICY "Admins can delete content creator profiles"
ON user_content_creator
FOR DELETE
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

-- Create RLS policies for user_alumni
CREATE POLICY "Admins can view all alumni profiles"
ON user_alumni
FOR SELECT
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

CREATE POLICY "Alumni can view their own profile"
ON user_alumni
FOR SELECT
USING (
  id = auth.uid()
);

CREATE POLICY "Admins can insert alumni profiles"
ON user_alumni
FOR INSERT
WITH CHECK (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

CREATE POLICY "Users can insert their own alumni profile during registration"
ON user_alumni
FOR INSERT
WITH CHECK (
  id = auth.uid()
);

CREATE POLICY "Admins can update alumni profiles"
ON user_alumni
FOR UPDATE
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

CREATE POLICY "Alumni can update their own profile"
ON user_alumni
FOR UPDATE
USING (
  id = auth.uid()
);

CREATE POLICY "Admins can delete alumni profiles"
ON user_alumni
FOR DELETE
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

-- Create RLS policies for user_companies
CREATE POLICY "Admins can view all company profiles"
ON user_companies
FOR SELECT
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

CREATE POLICY "Companies can view their own profile"
ON user_companies
FOR SELECT
USING (
  id = auth.uid()
);

CREATE POLICY "Admins can insert company profiles"
ON user_companies
FOR INSERT
WITH CHECK (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

CREATE POLICY "Users can insert their own company profile during registration"
ON user_companies
FOR INSERT
WITH CHECK (
  id = auth.uid()
);

CREATE POLICY "Admins can update company profiles"
ON user_companies
FOR UPDATE
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

CREATE POLICY "Companies can update their own profile"
ON user_companies
FOR UPDATE
USING (
  id = auth.uid()
);

CREATE POLICY "Admins can delete company profiles"
ON user_companies
FOR DELETE
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

-- Create internships table
CREATE TABLE IF NOT EXISTS internships (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  company_id UUID NOT NULL REFERENCES user_companies(id) ON DELETE CASCADE,
  name TEXT NOT NULL,
  description TEXT NOT NULL,
  duration TEXT NOT NULL,
  skills TEXT NOT NULL,
  is_approved BOOLEAN,
  rejection_reason TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Create trigger to automatically update updated_at timestamp for internships
CREATE TRIGGER update_internships_updated_at
BEFORE UPDATE ON internships
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Create RLS policies for internships
ALTER TABLE internships ENABLE ROW LEVEL SECURITY;

-- Companies can view their own internships
CREATE POLICY "Companies can view their own internships"
ON internships
FOR SELECT
USING (
  company_id = auth.uid()
);

-- Admins can view all internships
CREATE POLICY "Admins can view all internships"
ON internships
FOR SELECT
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

-- Companies can create internships
CREATE POLICY "Companies can create internships"
ON internships
FOR INSERT
WITH CHECK (
  company_id = auth.uid()
);

-- Admins can update internships (for approval/rejection)
CREATE POLICY "Admins can update internships"
ON internships
FOR UPDATE
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

-- Companies can update their own internships if not yet approved/rejected
CREATE POLICY "Companies can update their own internships if not yet approved/rejected"
ON internships
FOR UPDATE
USING (
  company_id = auth.uid() AND is_approved IS NULL
);

-- Admins can delete internships
CREATE POLICY "Admins can delete internships"
ON internships
FOR DELETE
USING (
  EXISTS (
    SELECT 1 FROM user_admins WHERE id = auth.uid()
  )
);

-- Companies can delete their own internships if not yet approved/rejected
CREATE POLICY "Companies can delete their own internships if not yet approved/rejected"
ON internships
FOR DELETE
USING (
  company_id = auth.uid() AND is_approved IS NULL
);

-- Function to approve an internship
CREATE OR REPLACE FUNCTION approve_internship(p_internship_id UUID)
RETURNS VOID AS $$
BEGIN
  UPDATE internships 
  SET is_approved = TRUE, rejection_reason = NULL
  WHERE id = p_internship_id;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Function to reject an internship with a reason
CREATE OR REPLACE FUNCTION reject_internship(p_internship_id UUID, p_rejection_reason TEXT)
RETURNS VOID AS $$
BEGIN
  UPDATE internships 
  SET is_approved = FALSE, rejection_reason = p_rejection_reason
  WHERE id = p_internship_id;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Insert a super admin user (this should be done manually)
-- Replace 'super_admin_user_id' with the actual UUID of the super admin user
-- INSERT INTO user_admins (id, full_name, is_super_admin)
-- VALUES ('super_admin_user_id', 'Super Admin', TRUE);
