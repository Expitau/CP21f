package server;

import course.*;
import utils.Config;
import utils.ErrorCode;
import utils.Pair;

import java.io.*;
import java.nio.*;
import java.util.*;

public class Server {

    private static final String COURSE_PATH = "data/Courses/2020_Spring/";

    private boolean checkCondition(Course course, Map<String,Object> searchConditions){
        String dc = null, nc = null;
        Integer ac = null;

        if(searchConditions != null){
            dc = (String) searchConditions.get("dept");
            ac = (Integer) searchConditions.get("ay");
            nc = (String) searchConditions.get("name");
        }

        if(dc != null && !course.department.equals(dc)) return false;
        if(nc != null && !(Arrays.asList(course.courseName.split("\\s"))).containsAll(Arrays.asList(nc.split("\\s")))) return false;
        if(ac != null && course.academicYear > ac) return false;
        return true;
    }

    private File[] getValidFiles(File file){
        return file.listFiles(new FileFilter(){
            @Override
            public boolean accept(File file) {
                return !file.isHidden();
            }
        });
    }

    private Course getCourse(String coursePath){
        Course course = null;
        
        try (Scanner input = new Scanner(new File(coursePath))){
            String content = input.nextLine();
            String[] contentPices = content.split("\\|");

            String department = contentPices[0];
            String academicDegree = contentPices[1];
            Integer academicYear = Integer.parseInt(contentPices[2]);
            String courseName = contentPices[3];
            Integer credit = Integer.parseInt(contentPices[4]);
            String location = contentPices[5];
            String instructor = contentPices[6];
            Integer quota = Integer.parseInt(contentPices[7]);
            
            String[] pathPices = coursePath.split("\\/");
            String college = pathPices[3];
            Integer courseId = Integer.parseInt(pathPices[4].split("\\.")[0]);

            course = new Course(courseId, college, department, academicDegree, academicYear, courseName, credit, location, instructor, quota);
        } catch (Exception e){

        }

        return course;
    }

    private List<Course> getFilteredCollegeCourses(String collage,  Map<String,Object> searchConditions){
        String collagePath = COURSE_PATH + collage + "/";
        
        File collageDir = new File(collagePath);
        File[] courseFiles = getValidFiles(collageDir);
        List<Course> courses = new LinkedList<>();

        for(File courseFile : courseFiles){
            Course course = getCourse(courseFile.getPath());

            if(checkCondition(course, searchConditions))
                courses.add(course);
        }
        return courses;
    }

    private List<Course> getFilteredCourses(Map<String, Object> searchConditions){
        File courseDir = new File(COURSE_PATH);
        File[] collageFiles = getValidFiles(courseDir);
        List<Course> courses = new LinkedList<>();

        for(File collageFile : collageFiles){
            courses.addAll(getFilteredCollegeCourses(collageFile.getName(), searchConditions));
        }

        return courses;
    }

    private Comparator<Course> getCourseComparator(String sortCriteria){
        Comparator<Course> cmp = new Comparator<Course>(){
            @Override
            public int compare(Course c1, Course c2){
                return Integer.compare(c1.courseId, c2.courseId);
            }
        };   
        if(sortCriteria != null){
            switch(sortCriteria){
                case "name":
                    cmp = new Comparator<Course>(){
                        @Override
                        public int compare(Course c1, Course c2){
                            int res = c1.courseName.compareTo(c2.courseName);
                            if(res == 0) return Integer.compare(c1.courseId, c2.courseId);
                            return res;
                        }
                    };
                    break;
                case "dept":
                    cmp = new Comparator<Course>(){
                        @Override
                        public int compare(Course c1, Course c2){
                            int res = c1.department.compareTo(c2.department);
                            if(res == 0) return Integer.compare(c1.courseId, c2.courseId);
                            return res;
                        }
                    };
                    break;
                case "ay":
                    cmp = new Comparator<Course>(){
                        @Override
                        public int compare(Course c1, Course c2){
                            int res = Integer.compare(c1.academicYear, c2.academicYear);
                            if(res == 0) return Integer.compare(c1.courseId, c2.courseId);
                            return res;
                        }
                    };
                    break;    
            }
        }
        

        return cmp;
    }

    public List<Course> search(Map<String,Object> searchConditions, String sortCriteria){ // Problem 2-1
        List<Course> courses = new ArrayList<>(getFilteredCourses(searchConditions));
        Comparator<Course> cmp = getCourseComparator(sortCriteria);
        Collections.sort(courses, cmp);

        return courses;
    }

    public int bid(int courseId, int mileage, String userId){ // Problem 2-2
        
        return ErrorCode.IO_ERROR;
    }

    public Pair<Integer,List<Bidding>> retrieveBids(String userId){ // Problem 2-2
        
        return new Pair<>(ErrorCode.IO_ERROR,new ArrayList<>());
    }

    public boolean confirmBids(){ // Problem 2-3
        
        return false;
    }

    public Pair<Integer,List<Course>> retrieveRegisteredCourse(String userId){ // Problem 2-3
        
        return new Pair<>(ErrorCode.IO_ERROR,new ArrayList<>());
    }
}