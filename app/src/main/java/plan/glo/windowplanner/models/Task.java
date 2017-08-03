package plan.glo.windowplanner.models;
import java.util.Date;
import java.util.List;

public class Task implements TaskI{

   private List<Job> jobs;
   private Date startDate;
   private Date endDate;
   private String name;

   @Override
   public int getId() {
      return 0;
   }

   @Override
   public String getTaskName() {
      return null;
   }

   @Override
   public Date getTaskEndDate() {
      return null;
   }

   @Override
   public Date getTaskStartDate() {
      return null;
   }

   @Override
   public List<JobI> getJobs() {
      return null;
   }

   @Override
   public void modifyTask(Task configObject) {

   }
}
