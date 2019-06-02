package hr.ferit.brunozoric.taskie.ui.fragments

import android.os.Bundle
import android.view.View
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.Taskie.Companion.instance
import hr.ferit.brunozoric.taskie.common.EXTRA_TASK_ID
import hr.ferit.brunozoric.taskie.common.RESPONSE_OK
import hr.ferit.brunozoric.taskie.common.displayToast
import hr.ferit.brunozoric.taskie.common.gone
import hr.ferit.brunozoric.taskie.model.BackendTask
import hr.ferit.brunozoric.taskie.model.PriorityColor
import hr.ferit.brunozoric.taskie.model.Task
import hr.ferit.brunozoric.taskie.model.request.DeleteTaskRequest
import hr.ferit.brunozoric.taskie.model.request.TaskByIdRequest
import hr.ferit.brunozoric.taskie.model.response.GetTasksResponse
import hr.ferit.brunozoric.taskie.model.response.TaskByIdResponse
import hr.ferit.brunozoric.taskie.networking.BackendFactory
import hr.ferit.brunozoric.taskie.persistence.Repository
import hr.ferit.brunozoric.taskie.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_task_details.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskDetailsFragment : BaseFragment(), UpdateTaskFragmentDialog.TaskUpdatedListener {


    private val repository = Repository
    private var taskID = NO_TASK
    private val interactor = BackendFactory.getTaskieInteractor()

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_task_details
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(EXTRA_TASK_ID)?.let { taskID = it }
        tryDisplayTask(taskID)
    }

    override fun onResume() {
        super.onResume()
        tryDisplayTask(taskID)
    }

    private fun tryDisplayTask(taskId: String) {
        try {
            interactor.getTask(TaskByIdRequest(taskId), callBack())
        } catch (e: NoSuchElementException) {
            context?.displayToast(getString(R.string.noTaskFound))
        }
    }

    fun onTaskUpdateF(title: String, content: String, priority: Int, id: String) {
        val dialog = UpdateTaskFragmentDialog.newInstance()
        dialog.setTaskUpdatedListener(this)
        dialog.setCurrentTask(title, content, priority, id)
        dialog.show(childFragmentManager, dialog.tag)
    }

    override fun onTaskUpdate(title: String, content: String, priority: Int, id: String) {
        detailsTaskTitle.text = title
        detailsTaskDescription.text = content
        detailsPriorityView.setBackgroundResource(
            when (priority) {
                1 -> PriorityColor.LOW.getColor()
                2 -> PriorityColor.MEDIUM.getColor()
                else -> PriorityColor.HIGH.getColor()
            }
        )
        update.setOnClickListener { onTaskUpdateF(title, content, priority, id) }
        this.activity?.onBackPressed()

    }

    private fun callBack(): Callback<BackendTask> = object : Callback<BackendTask> {
        override fun onFailure(call: Call<BackendTask>, t: Throwable) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onResponse(call: Call<BackendTask>, response: Response<BackendTask>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse(response)
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleSomethingWentWrong() = instance.displayToast("Something went wrong!")

    private fun handleOkResponse(response: Response<BackendTask>) {
        response.body()?.run {
            detailsTaskTitle.text = title
            detailsTaskDescription.text = content
            detailsPriorityView.setBackgroundResource(
                when (taskPriority) {
                    1 -> PriorityColor.LOW.getColor()
                    2 -> PriorityColor.MEDIUM.getColor()
                    else -> PriorityColor.HIGH.getColor()
                }
            )
            update.setOnClickListener { onTaskUpdateF(title, content, taskPriority, id) }
        }

    }

    companion object {
        const val NO_TASK = " "

        fun newInstance(taskId: String): TaskDetailsFragment {
            val bundle = Bundle().apply { putString(EXTRA_TASK_ID, taskId) }
            return TaskDetailsFragment().apply { arguments = bundle }
        }
    }
}
