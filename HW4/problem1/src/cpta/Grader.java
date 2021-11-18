package cpta;

import cpta.environment.Compiler;
import cpta.environment.Executer;
import cpta.exam.ExamSpec;
import cpta.exceptions.*;
import cpta.exam.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Grader {
    Compiler compiler;
    Executer executer;

    public Grader(Compiler compiler, Executer executer) {
        this.compiler = compiler;
        this.executer = executer;
    }

    private File[] getFolders(File dirctory){
        return dirctory.listFiles(new FileFilter() { 
            @Override 
            public boolean accept(File dir) { 
                 return dir.isDirectory();
            }
        });
    }

    private File[] getFiles(File dirctory){
        return dirctory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file){
                return file.isFile();
            }
        });
    }

    private String[] getFileNameExtention(String sourceName) throws InvalidFileTypeException{
        String[] pieces = sourceName.split("\\.");
        if (pieces.length != 2) {
            throw new InvalidFileTypeException("File name is invalid.");
        }
        return pieces;
    }

    private boolean isSugoFile(String sourceName){
        try {
            String[] pieces = getFileNameExtention(sourceName);
            String fileExtension = pieces[1];
            if (fileExtension.toLowerCase().equals("sugo")) return true;
        } catch (Exception e) { }
        return false;
    }

    private boolean isYoFile(String sourceName){
        try {
            String[] pieces = getFileNameExtention(sourceName);
            String fileExtension = pieces[1];
            if (fileExtension.toLowerCase().equals("yo")) return true;
        } catch (Exception e) { }
        return false;
    }

    private String getSourceFileName(String execName) throws InvalidFileTypeException{
        if (!isYoFile(execName)) {
            throw new InvalidFileTypeException("File does not have .sugo extension.");
        }

        String[] pieces = getFileNameExtention(execName);
        String fileName = pieces[0];
        return fileName + ".sugo";
    }

    private String getExecFileName(String sourceName) throws InvalidFileTypeException{
        if (!isSugoFile(sourceName)) {
            throw new InvalidFileTypeException("File does not have .sugo extension.");
        }

        String[] pieces = getFileNameExtention(sourceName);
        String fileName = pieces[0];
        return fileName + ".yo";
    }

    private String getUniformFileContent(String FilePath, Set<String> judgingTypes){
        String targetContent = "";
        try(Scanner input = new Scanner(new File(FilePath))){
            targetContent = input.useDelimiter("\\Z").next();
            if(judgingTypes.contains(Problem.LEADING_WHITESPACES)) targetContent = targetContent.stripLeading();
            if(judgingTypes.contains(Problem.IGNORE_WHITESPACES)) targetContent = targetContent.replaceAll("\\s", "");
            if(judgingTypes.contains(Problem.CASE_INSENSITIVE)) targetContent = targetContent.toLowerCase();
            if(judgingTypes.contains(Problem.IGNORE_SPECIAL_CHARACTERS)) targetContent = targetContent.replaceAll("[^a-zA-Z0-9\\s]", "");
        }catch(Exception e){

        }
        return targetContent;
    }

    private Double scoreResult(String targetFilePath, String answerFilePath, Double score, Set<String> judgingTypes){
        String targetContent = getUniformFileContent(targetFilePath, judgingTypes);
        String answerContent = getUniformFileContent(answerFilePath, judgingTypes);

        if(targetContent.equals(answerContent)) return score;
        return Double.valueOf(0);
    }


    private Double scoreTestCase(Problem problem, TestCase testCase, String problemSubPath, String execPath)
    throws RunTimeErrorException, InvalidFileTypeException, FileSystemRelatedException{
        String testCasesDirPath = problem.testCasesDirPath;
        String testCaseInputPath = testCasesDirPath + testCase.inputFileName + "/";
        String testCaseOutputPath = testCasesDirPath + testCase.outputFileName + "/";
        String resultOutputPath = problemSubPath + testCase.outputFileName + "/";

        executer.execute(execPath, testCaseInputPath, resultOutputPath);

        return scoreResult(resultOutputPath, testCaseOutputPath, testCase.score, problem.judgingTypes);
    }

    private File[] getSugoFiles(String path){
        File dir = new File(path);
        return dir.listFiles(new FileFilter() {
            public boolean accept(File dir) { 
                if(dir.isDirectory()) return false;
                return isSugoFile(dir.getName()); 
           }
        });
    }

    private File[] getYoFiles(String path){
        File dir = new File(path);
        return dir.listFiles(new FileFilter() {
            public boolean accept(File dir) { 
                if(dir.isDirectory()) return false;
                return isYoFile(dir.getName()); 
           }
        });
    }

    private void copyWrapperFiles(String wrappersDirPath, String problemSubPath){
        try {
            File[] wrapperFiles = getFiles(new File(wrappersDirPath));
            for(File file : wrapperFiles){
                Path in = file.toPath();
                Path out = Paths.get(problemSubPath + file.getName() + "/");
                Files.copy(in, out);
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    private String getStudentSubPath(String submissionDirPath, String studentId){
        File[] folders = getFolders(new File(submissionDirPath));
        for(File dir : folders){
            if(dir.getName().contains(studentId)){
                return submissionDirPath + dir.getName() + "/";
            }
        }
        return null;
    }

    private String getProblemSubPath(String studentSubPath, String problemId){
        String problemSubPath = studentSubPath + problemId + "/";
        if(!(new File(problemSubPath).exists())) return null;
        File[] folders = getFolders(new File(problemSubPath));

        if(folders.length > 1) return null;
        else if(folders.length == 1){
            File[] files = getFiles(folders[0]);
            for(File file : files){
                Path in = file.toPath();
                Path out = Paths.get(problemSubPath + file.getName() + "/");
                try{
                    Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
                }catch(Exception e){
                    // handle it!!
                }
            }
        }

        return problemSubPath;
    }

    private void compileAllFile(String problemSubPath) 
    throws CompileErrorException, InvalidFileTypeException, FileSystemRelatedException{
        File[] sugoFiles = getSugoFiles(problemSubPath);
        for(File file : sugoFiles) compiler.compile(file.getPath());
    }

    private List<Double> scoreProblem(String studentSubPath, Problem problem, String submissionDirPath){
        List<Double> scores = new LinkedList<>();
        String problemSubPath, execPath;
        Double hasSugoFile = Double.valueOf(1);

        try{
            if(studentSubPath == null) throw new Exception();

            problemSubPath = getProblemSubPath(studentSubPath, problem.id);
            if(problemSubPath == null) throw new Exception();

            if(problem.wrappersDirPath != null) copyWrapperFiles(problem.wrappersDirPath, problemSubPath);

            compileAllFile(problemSubPath);

            if(getSugoFiles(problemSubPath).length < getYoFiles(problemSubPath).length) hasSugoFile /= 2;
            
            execPath = problemSubPath + getExecFileName(problem.targetFileName) + "/";
        }catch(Exception e){
            for(int i=0; i<problem.testCases.size(); i++) scores.add(Double.valueOf(0));
            return scores;
        }
        
        for(TestCase testCase : problem.testCases){
            try {
                scores.add(scoreTestCase(problem, testCase, problemSubPath, execPath) * hasSugoFile);
            } catch (Exception e) {
                scores.add(Double.valueOf(0));
            }
        }
        return scores;
    }

    private Map<String, List<Double>> getStudentScore(Student student, List<Problem> problems, String submissionDirPath){
        Map<String, List<Double>> problemScores = new HashMap<>();
        String studentSubPath = getStudentSubPath(submissionDirPath, student.id);
        for(Problem problem : problems){
            problemScores.put(problem.id, scoreProblem(studentSubPath, problem, submissionDirPath));
        }
        return problemScores;
    }


    public Map<String,Map<String, List<Double>>> gradeSimple(ExamSpec examSpec, String submissionDirPath) { // Problem 1-1
        List<Problem> problems = examSpec.problems;
        List<Student> students = examSpec.students;
        Map<String,Map<String, List<Double>>> ret = new HashMap<>();

        for(Student student : students){
            ret.put(student.id, getStudentScore(student, problems, submissionDirPath));
        }
        
        return ret;
    }

    public Map<String,Map<String, List<Double>>> gradeRobust(ExamSpec examSpec, String submissionDirPath) { // Problem 1-2
        List<Problem> problems = examSpec.problems;
        List<Student> students = examSpec.students;
        Map<String,Map<String, List<Double>>> ret = new HashMap<>();

        for(Student student : students){
            ret.put(student.id, getStudentScore(student, problems, submissionDirPath));
        }
        
        return ret;
    }
}

