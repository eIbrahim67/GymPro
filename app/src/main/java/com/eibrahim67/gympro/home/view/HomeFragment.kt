package com.eibrahim67.gympro.home.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.preference.PreferenceManager
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.databinding.FragmentHomeBinding
import com.eibrahim67.gympro.home.view.adapters.AdapterRVFeaturedPlans
import com.eibrahim67.gympro.home.view.adapters.AdapterRVTrainers
import com.eibrahim67.gympro.home.view.bottomSheet.ItemListDialogSeeMorePlans
import com.eibrahim67.gympro.home.viewModel.HomeViewModel
import com.eibrahim67.gympro.home.viewModel.HomeViewModelFactory
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapterRVFeaturedPlans = AdapterRVFeaturedPlans { id -> goToTrainPlan(id) }
    private val adapterRVTrainers = AdapterRVTrainers { id -> chatWithTrainer(id) }

    private val utils = UtilsFunctions

    lateinit var preferenceManager: PreferenceManager
    private var workoutNumber = -1
    private var exercisesNumber = -1
    private var repsNumber = -1
    private var todayNumOfTime = -1

    lateinit var seeMorePlansBottomSheet: ItemListDialogSeeMorePlans

    val repsMap = mutableMapOf<String, Int>()
    var list: List<Int>? = listOf<Int>()
    var workout: Workout? = null

    private val viewModel: HomeViewModel by viewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        HomeViewModelFactory(remoteRepository, userRepository)
    }

    private val sharedViewModel: MainViewModel by activityViewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        MainViewModelFactory(userRepository, remoteRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
        initSetOnClickListener()
    }

    private fun initUi() {
        preferenceManager = PreferenceManager(requireContext())

        seeMorePlansBottomSheet = ItemListDialogSeeMorePlans { id -> goToTrainPlan(id) }

        binding.recyclerviewFeaturePlan.adapter = adapterRVFeaturedPlans
        binding.recyclerviewOurTrainers.adapter = adapterRVTrainers
        binding.beginTraining.text = if (todayNumOfTime > 0) "Let’s Finish This!"
        else if (preferenceManager.getWorkoutDone()) "We Finish Today!" else "Let’s Get Started!"
    }

    private fun initObservers() {

        viewModel.getMyTrainPlan()
        viewModel.myTrainPlan.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    binding.todayWorkoutTitle.text = "${response.data?.name}"
                    list = response.data?.workoutsIds

                    if (preferenceManager.getDayNumber() != preferenceManager.getTodayKey()) {
                        list?.let {
                            when (preferenceManager.newDay(it.size)) {
                                0 -> {
                                    snackbar("It's a new day with new workout.")
                                }

                                1 -> {
                                    AlertDialog.Builder(requireContext())
                                        .setTitle("Unfinished Workout")
                                        .setMessage("It's a new day, but you didn't complete your workout yesterday. What would you like to do?")

                                        .setPositiveButton("Finish") { _, _ ->
                                            list?.let { preferenceManager.finishWorkout(it.size) }

                                            list?.let {
                                                workoutNumber =
                                                    preferenceManager.getWorkoutNumber(it.size)
                                            }

                                            list?.let { sharedViewModel.getWorkoutById(it[workoutNumber]) }

                                        }.setNegativeButton("Re-Train") { _, _ ->
                                            preferenceManager.reTrainWorkout()
                                            list?.let {
                                                workoutNumber =
                                                    preferenceManager.getWorkoutNumber(it.size)
                                            }
                                            list?.let { sharedViewModel.getWorkoutById(it[workoutNumber]) }
                                        }.setCancelable(false).show()
                                }

                                2 -> {
                                    snackbar("You missed workout yesterday. Ready to catch up?")
                                }

                                else -> {
                                    snackbar("Something Wrong")
                                }
                            }
                        }

                    }

                    list?.let { workoutNumber = preferenceManager.getWorkoutNumber(it.size) }
                    list?.let { sharedViewModel.getWorkoutById(it[workoutNumber]) }

                    if (preferenceManager.getWorkoutDone()) {

                        binding.workoutProgressText.text =
                            "${workoutNumber + 1}/${list?.size}\nWorkout"

                        list?.let {
                            binding.workoutProgressIndicator.progress =
                                (((workoutNumber + 1).toDouble() / it.size) * 100).toInt()
                        }
                    }

                }

                is ResponseEI.Failure -> {}
            }
        }

        sharedViewModel.workoutById.observe(viewLifecycleOwner) { resultWorkout ->
            when (resultWorkout) {
                is ResponseEI.Loading -> {
                    list?.let { workoutNumber = preferenceManager.getWorkoutNumber(it.size) }
                    exercisesNumber = preferenceManager.getExercisesNumber()
                    repsNumber = preferenceManager.getRepsNumber()
                    todayNumOfTime = preferenceManager.getTimeSpent()
                }

                is ResponseEI.Success -> {
                    workout = resultWorkout.data
                    workout?.let {
                        for (e in it.exerciseIds) {
                            sharedViewModel.getExerciseById(e)
                        }
                    }
                    workout?.let { binding.todayWorkoutName.text = "-> ${it.name}" }
                    //===========================================
                    binding.workoutProgressText.text = "${workoutNumber}/${list?.size}\nWorkout"
                    list?.let {
                        binding.workoutProgressIndicator.progress =
                            ((workoutNumber.toDouble() / it.size) * 100).toInt()
                    }
                    //===========================================
                    workout?.exerciseIds?.let {
                        binding.todayNumOfExercises.text =
                            "${exercisesNumber}/${it.size}\nExercises"
                    }
                    workout?.exerciseIds?.let {
                        binding.todayExercisesLinearProgressIndicator.progress =
                            ((exercisesNumber.toDouble() / it.size) * 100.0).toInt()
                    }
                    //===========================================
                    binding.todayNumOfTime.text =
                        "${todayNumOfTime}/${workout?.durationMinutes}\nMins"
                    workout?.let {
                        binding.todayTimeLinearProgressIndicator.progress =
                            ((todayNumOfTime.toDouble() / it.durationMinutes) * 100).toInt()
                    }
                }

                is ResponseEI.Failure -> createFailureResponse(resultWorkout, requireContext())
            }
        }

        sharedViewModel.exerciseById.observe(viewLifecycleOwner) { exercises ->
            when (exercises) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    exercises.data?.let { exercise ->
                        repsMap[exercise.id.toString()] =
                            (exercise.exerciseReps * exercise.exerciseSet)

                        binding.todayNumOfReps.text = "${repsNumber}/${repsMap.values.sum()}\nReps"
                        binding.todayRepsLinearProgressIndicator.progress =
                            ((repsNumber.toDouble() / repsMap.values.sum()) * 100).toInt()
                    }
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(exercises.reason), requireContext())
                }
            }
        }

        sharedViewModel.getTrainPlans()
        sharedViewModel.trainPlans.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Success -> {
                    adapterRVFeaturedPlans.submitList(response.data)
                }

                is ResponseEI.Failure -> utils.createFailureResponse(response, requireContext())
                else -> {}
            }
        }
        viewModel.getAllCoaches()
        viewModel.coaches.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {
                }

                is ResponseEI.Success -> {
                    adapterRVTrainers.submitList(response.data)
                }

                is ResponseEI.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }
        }

        viewModel.getCurrentDate()
        viewModel.currentDate.observe(viewLifecycleOwner) { date ->
            when (date) {
                is ResponseEI.Success -> binding.currentDate.text = date.data
                is ResponseEI.Failure -> utils.createFailureResponse(date, requireContext())
                else -> {}
            }
        }

        viewModel.getHelloSate()
        viewModel.helloSate.observe(viewLifecycleOwner) { date ->
            when (date) {
                is ResponseEI.Success -> binding.textViewHello.text = date.data
                is ResponseEI.Failure -> utils.createFailureResponse(date, requireContext())
                else -> {}
            }
        }
    }

    private fun initSetOnClickListener() {

        binding.beginTraining.setOnClickListener {

            sharedViewModel.navigateRightTo(R.id.action_train)
        }

        binding.chatbotLayout.setOnClickListener {
            sharedViewModel.navigateRightTo(R.id.action_action_home_to_chatbot)
        }

        binding.profileLayout.setOnClickListener {
            sharedViewModel.navigateLeftTo(R.id.action_profile)
        }

        binding.textViewFeaturePlanSeeMore.setOnClickListener {
            seeMorePlansBottomSheet.show(
                requireActivity().supportFragmentManager, "SeeMoreBottomSheet"
            )
        }
    }

    private fun goToTrainPlan(id: Int) {
        sharedViewModel.setTrainPlanId(id)
        sharedViewModel.navigateRightTo(R.id.action_showTrainPlan)
    }

    private fun chatWithTrainer(id: Int) {
        sharedViewModel.setChatWithId(id)
        sharedViewModel.navigateRightTo(R.id.action_chat)
    }

    fun snackbar(msg: String) = Snackbar.make(
        requireView(), msg, Snackbar.LENGTH_INDEFINITE
    ).setAction("OK") { }.show()


}
