CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE INSERT OR UPDATE ON public.user_students
    FOR EACH ROW
EXECUTE FUNCTION public.update_updated_at();

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE INSERT OR UPDATE ON public.user_companies
    FOR EACH ROW
EXECUTE FUNCTION public.update_updated_at();

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE INSERT OR UPDATE ON public.user_alumni
    FOR EACH ROW
EXECUTE FUNCTION public.update_updated_at();

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE INSERT OR UPDATE ON public.user_admins
    FOR EACH ROW
EXECUTE FUNCTION public.update_updated_at();

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE INSERT OR UPDATE ON public.user_content_creators
    FOR EACH ROW
EXECUTE FUNCTION public.update_updated_at();

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE INSERT OR UPDATE ON public.internships
    FOR EACH ROW
EXECUTE FUNCTION public.update_updated_at();

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE INSERT OR UPDATE ON public.courses
    FOR EACH ROW
EXECUTE FUNCTION public.update_updated_at();

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE INSERT OR UPDATE ON public.course_videos
    FOR EACH ROW
EXECUTE FUNCTION public.update_updated_at();

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE INSERT OR UPDATE ON public.course_changes
    FOR EACH ROW
EXECUTE FUNCTION public.update_updated_at();

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE INSERT OR UPDATE ON public.video_changes
    FOR EACH ROW
EXECUTE FUNCTION public.update_updated_at();




