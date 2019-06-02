package hr.ferit.brunozoric.taskie.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.common.RESPONSE_OK
import hr.ferit.brunozoric.taskie.common.displayToast
import hr.ferit.brunozoric.taskie.model.BackendTask
import hr.ferit.brunozoric.taskie.model.PriorityColor
import hr.ferit.brunozoric.taskie.model.request.UpdateTaskRequest
import hr.ferit.brunozoric.taskie.networking.BackendFactory
import hr.ferit.brunozoric.taskie.persistence.Repository
import kotlinx.android.synthetic.main.fragment_dialog_update_task.*
import kotlinx.android.synthetic.main.fragment_task_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateTaskFragmentDialog : DialogFragment() {

    private var taskUpdatedListener: TaskUpdatedListener? = null
    private val repository: Repository = Repository
    private val interactor = BackendFactory.getTaskieInteractor()
    private lateinit var currentTitle: String
    private lateinit var currentDesc: String
    private var currentPrioritty: Int = 1
    private lateinit var currentID: String


    interface TaskUpdatedListener {
        fun onTaskUpdate(title: String, content: String, priority: Int, id: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FragmentDialogTheme)
    }

    fun setTaskUpdatedListener(listener: UpdateTaskFragmentDialog.TaskUpdatedListener) {
        taskUpdatedListener = listener
    }

    fun setCurrentTask(title: String, description: String, priority: Int, id: String) {
        currentTitle = title
        currentDesc = description
        currentPrioritty = priority
        currentID = id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_update_task, container)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initListeners()
    }

    private fun initUi() {
        context?.let {
            prioritySelectorUpdate.adapter =
                ArrayAdapter<PriorityColor>(it, android.R.layout.simple_spinner_dropdown_item, PriorityColor.values())
            prioritySelectorUpdate.setSelection(currentPrioritty - 1)
            updateTaskTitleInput.setText(currentTitle)
            updateTaskDescriptionInput.setText(currentDesc)
        }

    }

    private fun initListeners() {
        updateTaskAction.setOnClickListener { updateTask() }
    }

    private fun updateTask() {
        if (isInputEmpty()) {
            context?.displayToast(getString(R.string.emptyFields))
            return
        }

        val title = updateTaskTitleInput.text.toString()
        val description = updateTaskDescriptionInput.text.toString()
        val priority = prioritySelectorUpdate.selectedItemPosition + 1

        interactor.editTask(
            UpdateTaskRequest(currentID, title, description, priority),
            callBack()
        )

        clearUi()
       taskUpdatedListener?.onTaskUpdate(title, description, priority, currentID)
        dismiss()
    }

    private fun callBack(): Callback<BackendTask> = object : Callback<BackendTask> {
        override fun onFailure(call: Call<BackendTask>, t: Throwable) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onResponse(call: Call<BackendTask>, response: Response<BackendTask>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse(response)
                }
            }
        }

    }

    private fun handleOkResponse(response: Response<BackendTask>) {

    }


    private fun clearUi() {
        updateTaskTitleInput.text.clear()
        updateTaskDescriptionInput.text.clear()
        prioritySelectorUpdate.setSelection(0)
    }

    private fun isInputEmpty(): Boolean = TextUtils.isEmpty(updateTaskTitleInput.text) || TextUtils.isEmpty(
        updateTaskDescriptionInput.text
    )

    companion object {
        fun newInstance(): UpdateTaskFragmentDialog {
            return UpdateTaskFragmentDialog()
        }
    }


}