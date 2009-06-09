package fi.hut.soberit.agilefant.web;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionSupport;

import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.business.TransferObjectBusiness;
import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.transfer.TaskTO;
import flexjson.JSONSerializer;

@Component("taskAction")
@Scope("prototype")
public class TaskAction extends ActionSupport {

    private static final long serialVersionUID = 7699657599039468223L;
    
    // Services
    @Autowired
    private TaskBusiness taskBusiness;
    
    @Autowired
    private TransferObjectBusiness transferObjectBusiness;
    
    // Helper fields
    private Task task;
    private int taskId;
    private int backlogId;
    private int storyId;

    private Set<Integer> userIds = new HashSet<Integer>();
    
    private String jsonData;
    

    public String create() {
        setTask(new Task());
        return Action.SUCCESS;
    }
    
    public String ajaxStoreTask() {
        try {
            task = taskBusiness.storeTask(task, backlogId, storyId, userIds);
            populateJsonData();
        } catch (Exception e) {
            return CRUDAction.AJAX_ERROR;
        }
        return CRUDAction.AJAX_SUCCESS;
    }
    
    public String ajaxDeleteTask() {
        try {
            taskBusiness.delete(taskId);
        } catch (Exception e) {
            return CRUDAction.AJAX_ERROR;
        }
        return CRUDAction.AJAX_SUCCESS;
    }
    
    private void populateJsonData() {
        TaskTO taskTO = transferObjectBusiness.constructTaskTO(task);

        jsonData = new JSONSerializer().serialize(taskTO);
    }
    
    public String resetOriginalEstimate() {
        try {
            task = taskBusiness.resetOriginalEstimate(taskId);
        } catch (ObjectNotFoundException e) {
            addActionError(e.getMessage());
            return CRUDAction.AJAX_ERROR;
        }
        populateJsonData();
        return CRUDAction.AJAX_SUCCESS;
    }

    public String getTaskJSON() {
        this.task = this.taskBusiness.retrieveIfExists(taskId);
        if (this.task == null) {
            return CRUDAction.AJAX_ERROR;
        }
        populateJsonData();
        return CRUDAction.AJAX_SUCCESS;
    }
      
    // AUTOGENERATED    
    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
    
    public void setTaskBusiness(TaskBusiness taskBusiness) {
        this.taskBusiness = taskBusiness;
    }

    public void setBacklogId(int backlogId) {
        this.backlogId = backlogId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public Set<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Integer> userIds) {
        this.userIds = userIds;
    }
    
    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTransferObjectBusiness(
            TransferObjectBusiness transferObjectBusiness) {
        this.transferObjectBusiness = transferObjectBusiness;
    }
}