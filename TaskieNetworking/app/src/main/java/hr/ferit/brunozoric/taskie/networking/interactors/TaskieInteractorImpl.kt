package hr.ferit.brunozoric.taskie.networking.interactors

import hr.ferit.brunozoric.taskie.model.BackendTask
import hr.ferit.brunozoric.taskie.model.request.*
import hr.ferit.brunozoric.taskie.model.response.*
import hr.ferit.brunozoric.taskie.networking.TaskieApiService
import retrofit2.Callback

class TaskieInteractorImpl(private val apiService: TaskieApiService) : TaskieInteractor {

    override fun editTask(request: UpdateTaskRequest, updateTaskCallback: Callback<BackendTask>) {
        apiService.editTask(request).enqueue(updateTaskCallback)
    }

    override fun getTask(request: TaskByIdRequest, taskByIdCallBack: Callback<BackendTask>) {
        apiService.getTaskById(request.id).enqueue(taskByIdCallBack)
    }

    override fun delete(request: DeleteTaskRequest, deleteCallback: Callback<DeleteResponse>) {
        apiService.delete(request.id).enqueue(deleteCallback)
    }

    override fun getTasks(taskieResponseCallback: Callback<GetTasksResponse>) {
        apiService.getTasks().enqueue(taskieResponseCallback)
    }

    override fun register(request: UserDataRequest, registerCallback: Callback<RegisterResponse>) {
        apiService.register(request).enqueue(registerCallback)
    }

    override fun login(request: UserDataRequest, loginCallback: Callback<LoginResponse>) {
        apiService.login(request).enqueue(loginCallback)
    }

    override fun save(request: AddTaskRequest, saveCallback: Callback<BackendTask>) {
        apiService.save(request).enqueue(saveCallback)
    }
}