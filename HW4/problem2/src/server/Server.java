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
    private static final String USER_PATH = "data/Users/";

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

    private Course getCourseFromPath(String coursePath){
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

    private Course getCourseFromId(int courseId){
        File courseDir = new File(COURSE_PATH);
        File[] collegeFiles = getValidFiles(courseDir);
        for(File collegeFile : collegeFiles){
            File courseFile = new File(collegeFile, courseId + ".txt");
            if(courseFile.exists()){
                return getCourseFromPath(courseFile.getPath());
            }
        }
        return null;
    }

    private List<Course> getFilteredCollegeCourses(String college,  Map<String,Object> searchConditions){
        File collegeDir = new File(COURSE_PATH, college);
        File[] courseFiles = getValidFiles(collegeDir);
        List<Course> courses = new LinkedList<>();

        for(File courseFile : courseFiles){
            Course course = getCourseFromPath(courseFile.getPath());

            if(checkCondition(course, searchConditions))
                courses.add(course);
        }
        return courses;
    }

    private List<Course> getFilteredCourses(Map<String, Object> searchConditions){
        File courseDir = new File(COURSE_PATH);
        File[] collegeFiles = getValidFiles(courseDir);
        List<Course> courses = new LinkedList<>();

        for(File collegeFile : collegeFiles){
            courses.addAll(getFilteredCollegeCourses(collegeFile.getName(), searchConditions));
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

    private void updateBidsList(List<Bidding> bids, int courseId, int mileage){ // need check cousrId before!!!!!
        Iterator<Bidding> iter = bids.iterator();
        while(iter.hasNext()){
            Bidding bid = iter.next();
            if(bid.courseId == courseId){
                bid.mileage = mileage;
                if(mileage == 0) iter.remove();
                return;
            }
        }
        if(mileage != 0) bids.add(new Bidding(courseId, mileage));
    }

    private void writeBidTxt(List<Bidding> bids, String userId) throws IOException{
        File bidFile = new File(USER_PATH + userId + "/bid.txt");
        try(FileWriter output = new FileWriter(bidFile)){
            for(Bidding bid : bids){
                output.write(bid.courseId + "|" + bid.mileage + "\n");
            }
        }
    }

    public int bid(int courseId, int mileage, String userId){ // Problem 2-2
        if(mileage < 0) return ErrorCode.NEGATIVE_MILEAGE;
        if(mileage > Config.MAX_MILEAGE_PER_COURSE) return ErrorCode.OVER_MAX_COURSE_MILEAGE;
        
        Course course = getCourseFromId(courseId);
        if(course == null) return ErrorCode.NO_COURSE_ID;
        
        Pair<Integer, List<Bidding>> retBids = retrieveBids(userId);
        if(retBids.key != ErrorCode.SUCCESS) return retBids.key; // user_checked and IOEXCEPTIN
        
        updateBidsList(retBids.value, courseId, mileage);
        if(retBids.value.size() > Config.MAX_COURSE_NUMBER) return ErrorCode.OVER_MAX_COURSE_NUMBER;
        
        int totBids = 0;
        for(Bidding bid : retBids.value) totBids += bid.mileage;
        if(totBids > Config.MAX_MILEAGE) return ErrorCode.OVER_MAX_MILEAGE;
        
        try {
            writeBidTxt(retBids.value, userId);
        } catch (IOException e) {
            return ErrorCode.IO_ERROR;
        }

        return ErrorCode.SUCCESS;
    }

    public Pair<Integer,List<Bidding>> retrieveBids(String userId){ // Problem 2-2
        File userFile = new File(USER_PATH, userId);
        if(!userFile.exists()) return new Pair<>(ErrorCode.USERID_NOT_FOUND, new ArrayList<>());

        File bidsFile = new File(userFile, "bid.txt");
        List<Bidding> bids = new ArrayList<>();

        try(Scanner input = new Scanner(bidsFile)){
            while(input.hasNextLine()){
                String[] content = input.nextLine().split("\\|");
                int cousrId = Integer.parseInt(content[0]);
                int mileage = Integer.parseInt(content[1]);
                bids.add(new Bidding(cousrId, mileage));
            }
        }catch(IOException e){
            return new Pair<>(ErrorCode.IO_ERROR,new ArrayList<>());
        }
        return new Pair<>(ErrorCode.SUCCESS, bids);
    }



    public boolean confirmBids(){ // Problem 2-3
        
        return false;
    }

    public Pair<Integer,List<Course>> retrieveRegisteredCourse(String userId){ // Problem 2-3
        
        return new Pair<>(ErrorCode.IO_ERROR,new ArrayList<>());
    }
}