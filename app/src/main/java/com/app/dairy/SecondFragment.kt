package com.app.dairy

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*


class SecondFragment : Fragment(), MainActivity.OnFabClickedListener {

    private lateinit var btnAdd: FloatingActionButton

    private lateinit var cowNo: TextInputEditText
    private lateinit var serviceDate: MaterialButton
    private lateinit var method: TextInputEditText
    private lateinit var calvingDate: MaterialButton
    private lateinit var gender: MaterialButtonToggleGroup
    private lateinit var weight: TextInputEditText
    private lateinit var length: TextInputEditText
    private lateinit var production: TextInputEditText
    private lateinit var locationDate: MaterialButton
    private lateinit var daysDry: TextInputEditText
    private lateinit var peakYield: TextInputEditText
    private lateinit var peakYieldDate: MaterialButton
    private lateinit var servicePeriodDays: TextInputEditText
    private lateinit var calvingIntervalDays: TextInputEditText

    private lateinit var dateTimeFormatter: DateTimeFormatter
    private val datesMap = hashMapOf<String, String>()

    private val today = LocalDate.now()
    private lateinit var vm: ViewModel

    private var isModeView = false

    companion object {
        const val KEY_VIEW = "viewOnly"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        isModeView = arguments?.getBoolean(KEY_VIEW) ?: false

        val view = inflater.inflate(R.layout.fragment_second, container, false)
        vm = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

        btnAdd = requireActivity().findViewById(R.id.fabAdd)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cowNo = view.findViewById(R.id.textCowNo)

        method = view.findViewById(R.id.textMethod)
        gender = view.findViewById(R.id.genderToggleGroup)
        weight = view.findViewById(R.id.textWeightCalving)
        length = view.findViewById(R.id.textLength)
        production = view.findViewById(R.id.textProduction)
        daysDry = view.findViewById(R.id.textDaysDry)
        servicePeriodDays = view.findViewById(R.id.textDaysServicePeriod)
        calvingIntervalDays = view.findViewById(R.id.textDaysCalvingInterval)
        peakYield = view.findViewById(R.id.textPeakYield)


        serviceDate = view.findViewById(R.id.dateService)
        calvingDate = view.findViewById(R.id.dateCalving)
        locationDate = view.findViewById(R.id.dateLocation)
        peakYieldDate = view.findViewById(R.id.datePeakYield)

        // In view mode don't set listener
        if(isModeView) {

            vm.selectedItem.observe(viewLifecycleOwner) {
                it?.let { item ->

                    cowNo.setText(item.cowNo!!)
                    method.setText(item.method!!)

                    weight.setText(item.weight)
                    length.setText(item.length)
                    production.setText(item.production)
                    daysDry.setText(item.daysDry.toString())
                    servicePeriodDays.setText(item.servicePeriod.toString())
                    calvingIntervalDays.setText(item.calvingDays.toString())
                    peakYield.setText(item.peakYield)

                    when(item.gender!!) {
                        "F" -> view.findViewById<MaterialButton>(R.id.btnToggleFemale).isChecked = true
                        "M" -> view.findViewById<MaterialButton>(R.id.btnToggleMale).isChecked = true
                    }

                    serviceDate.setText(item.serviceDate)
                    calvingDate.setText(item.calvingDate)
                    locationDate.setText(item.locationDate)
                    peakYieldDate.setText(item.datePeakYield)
                }
            }
        } else {

            bindToPicker(serviceDate, Constants.KEY_SERVICE_DATE)
            bindToPicker(calvingDate, Constants.KEY_CALVING_DATE)
            bindToPicker(locationDate, Constants.KEY_LOCATION_DATE)
            bindToPicker(peakYieldDate, Constants.KEY_PEAK_YIELD_DATE)
        }


    }

    fun bindToPicker(btn: MaterialButton, dateKey: String): DatePickerDialog {

        val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val date = LocalDate.of(year, month, dayOfMonth)
            val text = date.format(dateTimeFormatter)
            datesMap[dateKey] = text
            btn.text = text
        }

        val picker = DatePickerDialog(
            requireContext(),
            listener, today.year, today.monthValue, today.dayOfMonth
        )

        btn.setOnClickListener {
            picker.show()
        }

        return picker
    }

    override fun onDetach() {
        super.onDetach()
        btnAdd.setImageDrawable(resources.getDrawable(R.drawable.ic_add, null))
        btnAdd.visibility = View.VISIBLE
        (requireActivity() as MainActivity).fabClickListener = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity() as MainActivity).fabClickListener = this
    }

    override fun isSuccessfullyFilled(): Boolean {

        if(!isModeView) {
            val selectedGender = when (gender.checkedButtonId) {
                R.id.btnToggleMale -> "M"
                else -> "F"
            }

            val item = DairyItem(
                cowNo.text.toString(),
                datesMap[Constants.KEY_SERVICE_DATE]!!,
                method.text.toString(),
                datesMap[Constants.KEY_CALVING_DATE]!!,
                selectedGender,
                weight.text.toString(),
                length.text.toString(),
                production.text.toString(),
                datesMap[Constants.KEY_LOCATION_DATE]!!,
                daysDry.text.toString().toInt(),
                peakYield.text.toString(),
                datesMap[Constants.KEY_PEAK_YIELD_DATE]!!,
                servicePeriodDays.text.toString(),
                calvingIntervalDays.text.toString().toInt()
            )

            vm.addToFb(item)
        }

        return true
    }

}