import java.util.ArrayList;

public class User {
    String userId;
    String userEmail;
    ArrayList<String> contacts;
    ArrayList<String> skills;
    String availability;
    String intro;
    String misc;
    ArrayList<Integer> pendingJoinRequests;
    ArrayList<Integer> pendingDecisions;
    ArrayList<Integer> currentProjects;
    ArrayList<Integer> pastProjects;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getMisc() {
        return misc;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }

    public ArrayList<Integer> getPendingJoinRequests() {
        return pendingJoinRequests;
    }

    public void setPendingJoinRequests(ArrayList<Integer> pendingJoinRequests) {
        this.pendingJoinRequests = pendingJoinRequests;
    }

    public ArrayList<Integer> getPendingDecisions() {
        return pendingDecisions;
    }

    public void setPendingDecisions(ArrayList<Integer> pendingDecisions) {
        this.pendingDecisions = pendingDecisions;
    }

    public ArrayList<Integer> getCurrentProjects() {
        return currentProjects;
    }

    public void setCurrentProjects(ArrayList<Integer> currentProjects) {
        this.currentProjects = currentProjects;
    }

    public ArrayList<Integer> getPastProjects() {
        return pastProjects;
    }

    public void setPastProjects(ArrayList<Integer> pastProjects) {
        this.pastProjects = pastProjects;
    }

    public ArrayList<Integer> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Integer> likes) {
        this.likes = likes;
    }

    ArrayList<Integer> likes;
}
