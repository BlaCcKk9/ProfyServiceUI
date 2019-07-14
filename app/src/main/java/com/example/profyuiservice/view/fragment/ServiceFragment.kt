package com.example.profyuiservice.view.fragment


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.profyuiservice.R
import com.example.profyuiservice.databinding.FragmentServiceBinding
import com.example.profyuiservice.util.ServiceClickListener
import com.example.profyuiservice.view.adapter.PhotoAdapter
import com.example.profyuiservice.view.adapter.ServiceAdapter
import com.example.profyuiservice.view.adapter.TitleAdapter
import com.example.profyuiservice.viewmodel.ServiceViewModel
import kotlinx.android.synthetic.main.fragment_service.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class ServiceFragment : Fragment(), ServiceClickListener {

    // ყველა საჭირო ცვლადი
    private lateinit var dataBinding: FragmentServiceBinding  // ამ ფრაგმენტის სერვის ბაინდინგი

    private lateinit var titleAdapter: TitleAdapter           //
    private lateinit var serviceAdapter: ServiceAdapter      //   ადაპტერები
    private lateinit var photoAdapter: PhotoAdapter          //

    private lateinit var serviceViewModel: ServiceViewModel  // სერვისის viewmodel-ი
    private lateinit var photoList: ArrayList<Bitmap>      // user-ის მიერ დამატებული სურათების ლისტი

    private var isDownClicked: Boolean = false              // შემოწმებისთვის საჭირო ცვლადი

    private val GALLERY = 1      // Gallery-ის კოდი
    private val CAMERA = 2       // Camera-ის კოდი
    private var numberCounter = 1  // მთვლელი რომელიც ითვის სერვისის რაოდენობას
    private var minute = 40       // ცვლადი რომელშიც არის წუთები სერვისის რაოდენობის მიხედვით


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate გავუკეთეთ ფრამგენტს რომელსაც აქვს DataBinding და დავაბრუნეთ
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_service, container, false)
        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // ინიციალიზაცია
        dataBinding.listener = this
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)
        photoAdapter = PhotoAdapter(arrayListOf())
        titleAdapter = TitleAdapter(arrayListOf())
        serviceAdapter = ServiceAdapter(arrayListOf())
        photoList = ArrayList()
        dataBinding.kilometers = numberCounter.toString()
        dataBinding.minutes = "$minute min"

        // ადაპტერების გაშვება
        titleRecyclerView.apply {
            adapter = titleAdapter
            layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        }
        serviceRecyclerView.apply {
            adapter = serviceAdapter
            layoutManager = LinearLayoutManager(context)
        }
        photoRecyclerView.apply {
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        }
        // Viewmodel-ის გაშვება სადაც გვაქვს ჩვენი სერვისის ლოკალური მონაცემები
        serviceViewModel.fetchService()

        // ობსერვ გავუკეთოთ ამ მონაცემებს
        observeServiceViewModel()

    }

    // ეს მეთოდი ამუშავდება მაშინ როცა user-ი დააჭერს ჩამოშლის ღილაკს
    override fun onScrollClicked(v: View) {
        // თუ სერვისები ჩამოშალა user-მა
        if(isDownClicked){
            serviceRecyclerView.visibility = View.GONE
            titleRecyclerView.visibility = View.VISIBLE
            scrollService.setImageResource(R.drawable.ic_arrow_down)
            isDownClicked = false
        } else { // თუ სერვისები ისევ უკან აწია user-მა
            serviceRecyclerView.visibility = View.VISIBLE
            titleRecyclerView.visibility = View.GONE
            // ვამოწმებთ ვერსიას background-ისთვის რატომღაც სხვანაირად ვერ გავაკეთე
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                scrollService.background = null

            scrollService.setImageResource(R.drawable.ic_arrow_up)
            isDownClicked = true
        }

    }
    // ამუშავდება მაშინ როცა user-ი დააჭეერს ფოტოს დამადების ღილაკს
    override fun onAddPhotoClicked(v: View) {
        showPictureDialog()
    }

    // ამუშავდება მაშინ როცა user-ი დააჭერს სერვისის მომატებისთვის პლუსის ღილაკს
    override fun onPlusClicked(v: View) {
        numberCounter++
        dataBinding.kilometers = numberCounter.toString()
        dataBinding.minutes = (minute * numberCounter).toString() + " min"

    }

    // ამუშავდება მაშინ როცა user-ი დააჭერს სერვისის მოკლებისთვის მინუსის ღილაკს
    override fun onMinusClicked(v: View) {
        if(numberCounter > 1) {
            numberCounter--
            dataBinding.kilometers = numberCounter.toString()
            dataBinding.minutes = (minute * numberCounter).toString() + " min"
        }

    }

    // observe გავუკეთეთ ჩვენს სერვისს
    private fun observeServiceViewModel(){
        serviceViewModel.service.observe(this, Observer {
            it?.let {
                // განახლება სერვის ლისტის
                titleAdapter.updateServiceList(it)
                serviceAdapter.updateService(it)
            }
        })
    }


    // მობილური დევაისიდან სურათების მიღება
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // თუ user-მა აირჩია გალერიდან სურათის დამატება
        if (requestCode == GALLERY){
            if (data != null)
            {
                val contentURI = data.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, contentURI)
                    Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, ByteArrayOutputStream())
                    photoList.add(bitmap)  // ლისტში დამატება სურათის
                    photoAdapter.updatePhotoList(photoList)  // დამატებული ფოტოების ლისტის განახლება ადაპტერში
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }
        }

        // თუ user-მა აირჩია კამერით გადაღება
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, ByteArrayOutputStream())
            photoList.add(thumbnail)   // ლისტში დამატება სურათის
            photoAdapter.updatePhotoList(photoList)   //დამატებული ფოტოების ლისტის განახლება ადაპტერში
            Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()
        }

    }

    // გალერიდან ამოღებისთვის პარამეტრების გადაცემა
    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    // კამერიდან გადაღებისთვის და ამოღებისთვის პარამეტრების გადაცემა
    private fun takePhotoFromCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    // მეთოდი ამოაგდებს დიალოგს, სადაც user-ი ირჩევს გალერიდან დაამატოს სურათი ან კამერიდან გადაიღოს
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }
}
