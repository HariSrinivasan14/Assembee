import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {
    int projectId;
    int ownerId;
    ArrayList<String> categories;
    String preferredAvailability;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getPreferredAvailability() {
        return preferredAvailability;
    }

    public void setPreferredAvailability(String preferredAvailability) {
        this.preferredAvailability = preferredAvailability;
    }

    public ArrayList<String> getDesiredSkills() {
        return desiredSkills;
    }

    public void setDesiredSkills(ArrayList<String> desiredSkills) {
        this.desiredSkills = desiredSkills;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<User> getContributors() {
        return contributors;
    }

    public void setContributors(ArrayList<User> contributors) {
        this.contributors = contributors;
    }

    ArrayList<String> desiredSkills;
    String description;
    ArrayList<User> contributors;
}
