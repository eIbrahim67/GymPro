package com.eibrahim67.gympro.showTrainPlan.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.mainActivity.viewModel.MainViewModel
import com.eibrahim67.gympro.showTrainPlan.view.adapters.AdapterRVWorkoutsTrainingPlan
import com.eibrahim67.gympro.showTrainPlan.viewModel.ShowTrainPlanViewModel
import com.eibrahim67.gympro.showTrainPlan.viewModel.ShowTrainPlanViewModelFactory
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class ShowTrainPlanFragment : Fragment() {

    private lateinit var trainPlanImage: ImageView
    private lateinit var trainPlanCoachName: TextView
    private lateinit var trainPlanTitle: TextView
    private lateinit var trainPlanDescription: TextView
    private lateinit var targetedMusclesText: TextView
    private lateinit var trainPlanDifficulty: TextView
    private lateinit var trainPlanAvgTime: TextView
    private lateinit var trainPlanDaysPerTrainingWeek: TextView
    private lateinit var recyclerviewWorkoutsTrainPlans: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView

    private val adapterRVWorkouts = AdapterRVWorkoutsTrainingPlan { id -> gotoWorkout(id) }

    private val viewModel: ShowTrainPlanViewModel by viewModels {
        ShowTrainPlanViewModelFactory()
    }

    private val sharedViewModel: MainViewModel by activityViewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        TrainViewModelFactory(userRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi(view)

        initObservers()

    }

    private fun initObservers() {
        sharedViewModel.trainPlanId.observe(viewLifecycleOwner) { id ->
            viewModel.getTrainingPlanById(id)
        }

        viewModel.trainPlan.observe(viewLifecycleOwner) { trainPlan ->
            when (trainPlan) {
                is Response.Loading -> {}
                is Response.Success -> {

                    Glide
                        .with(requireContext())
                        .load(trainPlan.data?.imageUrl)
                        .into(trainPlanImage)

                    sharedViewModel.getCoachById(trainPlan.data?.coachId ?: 0)

                    trainPlanTitle.text = trainPlan.data?.name
                    trainPlanDescription.text = trainPlan.data?.description

                    sharedViewModel.getTargetedMusclesByIds(
                        trainPlan.data?.targetedMuscleIds ?: emptyList()
                    )

                    trainPlanDifficulty.text = trainPlan.data?.difficultyLevel

                    trainPlanAvgTime.text =
                        "${trainPlan.data?.avgTimeMinPerWorkout.toString()} min"

                    trainPlanDaysPerTrainingWeek.text =
                        "${trainPlan.data?.durationDaysPerTrainingWeek.toString()} Days"

                    trainPlan.data?.workoutsIds?.let { ids -> sharedViewModel.getWorkoutsByIds(ids) }
                    Log.e("error5", trainPlan.data?.workoutsIds.toString())

                }

                is Response.Failure -> {
                    createFailureResponse(Response.Failure(trainPlan.reason), requireContext())
                }
            }

        }

        sharedViewModel.coachById.observe(viewLifecycleOwner) { coach ->
            when (coach) {
                is Response.Loading -> {}
                is Response.Success -> {

                    trainPlanCoachName.text = "by ${coach.data?.name}"
                }

                is Response.Failure -> {
                    createFailureResponse(Response.Failure(coach.reason), requireContext())
                }
            }

        }

        sharedViewModel.targetedMusclesByIds.observe(viewLifecycleOwner) { muscles ->
            when (muscles) {
                is Response.Loading -> {}
                is Response.Success -> {
                    targetedMusclesText.text = muscles.data
                }

                is Response.Failure -> {
                    createFailureResponse(Response.Failure(muscles.reason), requireContext())
                }
            }

        }

        sharedViewModel.workoutsByIds.observe(viewLifecycleOwner) { workouts ->

            when (workouts) {
                is Response.Loading -> {}
                is Response.Success -> {
                    adapterRVWorkouts.submitList(workouts.data ?: emptyList())
                    Log.e("error5", workouts.data.toString())
                }

                is Response.Failure -> {
                    createFailureResponse(Response.Failure(workouts.reason), requireContext())
                }
            }


        }
    }

    private fun initUi(view: View) {
        trainPlanImage = view.findViewById(R.id.trainPlanImage)
        trainPlanCoachName = view.findViewById(R.id.trainPlanCoachName)
        trainPlanTitle = view.findViewById(R.id.trainPlanTitle)
        targetedMusclesText = view.findViewById(R.id.targetedMusclesText)
        trainPlanDescription = view.findViewById(R.id.trainPlanDescription)
        trainPlanDifficulty = view.findViewById(R.id.trainPlanDifficulty)
        trainPlanAvgTime = view.findViewById(R.id.trainPlanAvgTime)
        trainPlanDaysPerTrainingWeek = view.findViewById(R.id.trainPlanDaysPerTrainingWeek)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE

        recyclerviewWorkoutsTrainPlans = view.findViewById(R.id.recyclerviewWorkoutsTrainPlans)
        recyclerviewWorkoutsTrainPlans.adapter = adapterRVWorkouts

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_show_train_plan, container, false)
    }

    private fun gotoWorkout(id: Int) {

        sharedViewModel.setWorkoutId(id)

        sharedViewModel.navigateTo(R.id.action_workout)

    }

}