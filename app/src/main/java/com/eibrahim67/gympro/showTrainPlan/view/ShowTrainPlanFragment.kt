package com.eibrahim67.gympro.showTrainPlan.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createMaterialAlertDialogBuilderOkCancel
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.showTrainPlan.view.adapters.AdapterRVWorkoutsTrainingPlan
import com.eibrahim67.gympro.showTrainPlan.viewModel.ShowTrainPlanViewModel
import com.eibrahim67.gympro.showTrainPlan.viewModel.ShowTrainPlanViewModelFactory
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.android.material.card.MaterialCardView

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
    private lateinit var trainPlanSetAsDefaultBtn: MaterialCardView
    private lateinit var backBtn: MaterialCardView

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
        iniListeners()
    }

    private fun iniListeners() {
        trainPlanSetAsDefaultBtn.setOnClickListener {
            createMaterialAlertDialogBuilderOkCancel(
                requireContext(),
                "Change train plan",
                "You will use this train plan as default?",
                "OK",
                "Cancel",
            ) {
                sharedViewModel.updateMyTrainPlan()
                sharedViewModel.updateMyCoachState(true)
            }
        }
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObservers() {
        sharedViewModel.trainPlanId.observe(viewLifecycleOwner) { id ->
            viewModel.getTrainingPlanById(id)
        }
        viewModel.trainPlan.observe(viewLifecycleOwner) { trainPlan ->
            when (trainPlan) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
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
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(trainPlan.reason), requireContext())
                }
            }
        }
        sharedViewModel.coachById.observe(viewLifecycleOwner) { coach ->
            when (coach) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    trainPlanCoachName.text = "by ${coach.data?.name}"
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(coach.reason), requireContext())
                }
            }
        }
        sharedViewModel.targetedMusclesByIds.observe(viewLifecycleOwner) { muscles ->
            when (muscles) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    targetedMusclesText.text = muscles.data
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(muscles.reason), requireContext())
                }
            }
        }
        sharedViewModel.workoutsByIds.observe(viewLifecycleOwner) { workouts ->
            when (workouts) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    adapterRVWorkouts.submitList(workouts.data ?: emptyList())
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(workouts.reason), requireContext())
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
        trainPlanSetAsDefaultBtn = view.findViewById(R.id.trainPlanSetAsDefaultBtn)
        backBtn = view.findViewById(R.id.trainPlanBackBtn)

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
        sharedViewModel.navigateRightTo(R.id.action_workout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.navigateRightTo(null)
    }
}