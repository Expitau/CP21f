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

    private File getUserFileFromID(String userId){
        File userFile = new File(USER_PATH, userId);
        if(!userFile.exists()) return null;
        return userFile;
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

    private int getTotalBidsMilage(List<Bidding> bids){
        int totBids = 0;
        for(Bidding bid : bids) totBids += bid.mileage;
        return totBids;
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
        
        if(getTotalBidsMilage(retBids.value) > Config.MAX_MILEAGE) return ErrorCode.OVER_MAX_MILEAGE;
        
        try {
            writeBidTxt(retBids.value, userId);
        } catch (IOException e) {
            return ErrorCode.IO_ERROR;
        }

        return ErrorCode.SUCCESS;
    }

    public Pair<Integer,List<Bidding>> retrieveBids(String userId){ // Problem 2-2
        File userFile = getUserFileFromID(userId);
        if(userFile == null) return new Pair<>(ErrorCode.USERID_NOT_FOUND, new ArrayList<>());

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


    private void writeRegTxt(Map<String, List<Integer>> regMap) throws IOException{
        for(String userId : regMap.keySet()){
            File userFile = getUserFileFromID(userId);
            File regFile = new File(userFile, "reg.txt");
            List<Integer> courses = regMap.get(userId);
            try(FileWriter output = new FileWriter(regFile)){
                for(Integer courseId : courses) output.write(courseId + "\n");
            }
        }
    }

    private Pair<Map<Integer, List<Pair<Integer, String>>>, Map<String, Integer>> getBidsInfo(){
        Map<Integer, List<Pair<Integer, String>>> courseMap = new HashMap<>();
        Map<String, Integer> totBidsMap = new HashMap<>(); 
        File[] userFiles = getValidFiles(new File(USER_PATH));
        for(File userFile : userFiles){
            String userId = userFile.getName();
            Pair<Integer, List<Bidding>> retBids = retrieveBids(userId);

            totBidsMap.put(userId, getTotalBidsMilage(retBids.value));

            if(retBids.key == ErrorCode.SUCCESS){
                for(Bidding bid : retBids.value){
                    Integer courseId = bid.courseId, mileage = bid.mileage;
                    if(courseMap.get(courseId) == null) courseMap.put(courseId, new ArrayList<>());
                    courseMap.get(courseId).add(new Pair<>(mileage, userId));
                }
            }else return new Pair<>(null, null);
        }
        return new Pair<>(courseMap, totBidsMap);
    }

    private Map<String, List<Integer>> getRegMap(Map<Integer, List<Pair<Integer, String>>> courseMap, Map<String, Integer> totBidsMap){
        Map<String, List<Integer>> regMap = new HashMap<>();
        for(Integer courseId : courseMap.keySet()){
            List<Pair<Integer, String>> users = courseMap.get(courseId);
            Course course = getCourseFromId(courseId);
            if(users == null) continue;
            if(users.size() > course.quota){
                Collections.sort(users, new Comparator<Pair<Integer, String>>(){
                    @Override
                    public int compare(Pair<Integer, String> p1, Pair<Integer, String> p2) {
                        int res = Integer.compare(p2.key, p1.key);
                        if(res != 0) return res;
                        res = Integer.compare(totBidsMap.get(p1.value), totBidsMap.get(p2.value));
                        if(res != 0) return res;
                        return p1.value.compareTo(p2.value);
                    }

                });
                users = users.subList(0, course.quota);
            }
            for(Pair<Integer, String> user : users){
                String userId = user.value;
                if(regMap.get(userId) == null) regMap.put(userId, new ArrayList<>());
                regMap.get(userId).add(courseId);
            }
        }
        return regMap;
    }

    public boolean confirmBids(){ // Problem 2-3
        // Pair<courseMap, totBidsMap);
        Pair<Map<Integer, List<Pair<Integer, String>>>,Map<String, Integer>> bidsInfo = getBidsInfo();
        if(bidsInfo.key == null) return false;

        try {writeRegTxt(getRegMap(bidsInfo.key, bidsInfo.value));}
        catch(IOException e) {return false;}
        
        return true;
    }

    public Pair<Integer,List<Course>> retrieveRegisteredCourse(String userId){ // Problem 2-3
        File userFile = getUserFileFromID(userId);
        if(userFile == null) return new Pair<>(ErrorCode.USERID_NOT_FOUND, new ArrayList<>());

        File regFile = new File(userFile, "reg.txt");
        
        List<Course> courses = new ArrayList<>();
        if(regFile.exists()){
            try(Scanner input = new Scanner(regFile)){
                while(input.hasNextLine()){
                    int courseId = Integer.parseInt(input.nextLine());
                    courses.add(getCourseFromId(courseId));
                }
            }catch(IOException e){
                return new Pair<>(ErrorCode.IO_ERROR,new ArrayList<>());
            }
        }

        return new Pair<>(ErrorCode.SUCCESS, courses);
    }
}