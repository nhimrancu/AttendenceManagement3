package arms.attendancemanagement.api;

public class Course {
    public int semester; // which semester's course
    public String code, title;
    public int credits;

    @Override
    public String toString() {

        return code;
    }
}
