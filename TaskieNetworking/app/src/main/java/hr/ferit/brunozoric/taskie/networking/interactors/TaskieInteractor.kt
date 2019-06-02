package hr.ferit.brunozoric.taskie.networking.interactors

import hr.ferit.brunozoric.taskie.model.BackendTask
import hr.ferit.brunozoric.taskie.model.request.*
import hr.ferit.brunozoric.taskie.model.response.*
import retrofit2.Callback

interface TaskieInteractor {

    fun getTasks(taskieResponseCallback: Callback<GetTasksResponse>)

    fun register(request: UserDataRequest, registerCallback: Callback<RegisterResponse>)

    fun login(request: UserDataRequest, loginCallback: Callback<LoginResponse>)

    fun getTask(request: TaskByIdRequest, taskByIdCallBack: Callback<BackendTask>)

    fun editTask(request: UpdateTaskRequest, updateTaskCallback: Callback<BackendTask>)

    fun save(request: AddTaskRequest, saveCallback: Callback<BackendTask>)

    fun delete(request: DeleteTaskRequest, deleteCallback: Callback<DeleteResponse>)

}