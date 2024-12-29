package com.example.ecommerce.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.ecommerce.CloudinaryManager;
import com.example.ecommerce.R;
import com.example.ecommerce.RegisterActivity;
import com.example.ecommerce.dao.CategoryDAO;
import com.example.ecommerce.dao.StoreDAO;
import com.example.ecommerce.dao.UserDAO;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.Store;
import com.example.ecommerce.entity.User;
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreDetailFragment extends Fragment {
    private static final String TAG = "Upload ###";
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private ImageView imgView_storeImage;
    private EditText edt_storeName, edt_storeAddress;
    private Spinner spinnerOwner;
    private Button btn_insert_ImageStore, btn_store_edit,
            btn_store_delete, btn_store_exit, btn_store_done, btn_store_insert;
    private String cloudinaryPath;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreDetailFragment newInstance(String param1, String param2) {
        StoreDetailFragment fragment = new StoreDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initConfig() {
//        Map config = new HashMap<>();
//        config.put("cloud_name", "dku2oqsgy");
//        config.put("api_key", "881488226939423");
//        config.put("api_secret", "XVTKFf5IFYzFv6GajJXeUpRuI70");
        CloudinaryManager.init(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initConfig();
        View view = inflater.inflate(R.layout.fragment_store_detail, container, false);
        imgView_storeImage = view.findViewById(R.id.imgView_store_image);
        edt_storeName = view.findViewById(R.id.edt_store_name);
        edt_storeAddress = view.findViewById(R.id.edt_store_address);
        spinnerOwner = view.findViewById(R.id.spinner_owner);
        btn_insert_ImageStore = view.findViewById(R.id.btn_insert_image_store);
        btn_store_insert = view.findViewById(R.id.btn_store_insert);
        btn_store_edit = view.findViewById(R.id.btn_store_edit);
        btn_store_delete = view.findViewById(R.id.btn_store_delete);
        btn_store_exit = view.findViewById(R.id.btn_store_exit);
        btn_store_done = view.findViewById(R.id.btn_store_done);

        btn_insert_ImageStore.setOnClickListener(v -> {
            requestPermissions();
        });

        // Set Adapter for Spinner
        UserDAO userDAO = new UserDAO(this.getContext());
        userDAO.open();
        List<User> sellers = userDAO.getUserByRole(Role.SELLER.getDisplayName());
        userDAO.close();
        List<String> usernameOfSellers = new ArrayList<>();
        for (User user: sellers) {
            usernameOfSellers.add(user.getUsername());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_spinner_item,
                usernameOfSellers
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOwner.setAdapter(adapter);

        // mở store detail
        Bundle bundleStore = getArguments();
        if (bundleStore != null) {
            String storeName = bundleStore.getString("storeName");
            String storeImage = bundleStore.getString("storeImage");
            String storeAddress = bundleStore.getString("storeAddress");
            int storeOwnerId = bundleStore.getInt("storeOwnerId");

            Glide.with(requireContext()).load(storeImage).into(imgView_storeImage);
            edt_storeName.setText(storeName);
            edt_storeAddress.setText(storeAddress);
            for (int i = 0; i < sellers.size(); i++) {
                if (sellers.get(i).getId() == storeOwnerId) {
                    spinnerOwner.setSelection(i);
                }
            }

            btn_store_insert.setVisibility(View.GONE);
            btn_store_edit.setVisibility(View.VISIBLE);
            btn_store_delete.setVisibility(View.VISIBLE);
            btn_insert_ImageStore.setVisibility(View.GONE);
            edt_storeName.setEnabled(false);
            edt_storeAddress.setEnabled(false);
            spinnerOwner.setEnabled(false);
        }

        // Set event click button edit
        btn_store_edit.setOnClickListener(v -> {
            edt_storeName.setEnabled(true);
            edt_storeAddress.setEnabled(true);

            btn_store_edit.setVisibility(View.GONE);
            btn_store_delete.setVisibility(View.GONE);
            btn_store_done.setVisibility(View.VISIBLE);
            btn_store_exit.setVisibility(View.VISIBLE);
        });

        // set event click done
        btn_store_done.setOnClickListener(v -> {
            String storeName = edt_storeName.getText().toString();
            String storeAddress = edt_storeAddress.getText().toString();
            int storeId = 0, storeOwnerId;
            String storeImage = "";
            User owner = new User();
            if (bundleStore != null) {
                storeId = bundleStore.getInt("storeId");
                storeImage = bundleStore.getString("storeImage");
                storeOwnerId = bundleStore.getInt("storeOwnerId");
                userDAO.open();
                owner = userDAO.getUserById(storeOwnerId);
            }
            Store store = new Store(storeId, storeName, storeAddress,storeImage, owner);
            if (storeName.isEmpty() || storeAddress.isEmpty() ) {
                Toast.makeText(this.getContext(), "Vui lòng không để trống thông tin", Toast.LENGTH_SHORT).show();
            } else {
                StoreDAO storeDAO = new StoreDAO(this.getContext());
                storeDAO.open();
                boolean result = storeDAO.updateStore(store);
                storeDAO.close();
                if (result) {
                    edt_storeName.setText(storeName);
                    edt_storeAddress.setText(storeAddress);
                    edt_storeName.setEnabled(false);
                    edt_storeAddress.setEnabled(false);
                    btn_store_done.setVisibility(View.GONE);
                    btn_store_exit.setVisibility(View.GONE);
                    btn_store_edit.setVisibility(View.VISIBLE);
                    btn_store_delete.setVisibility(View.VISIBLE);
                    Toast.makeText(this.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.getContext(), "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
        String oldName = edt_storeName.getText().toString().trim();
        String oldAddress = edt_storeAddress.getText().toString().trim();
        // set event click exit
        btn_store_exit.setOnClickListener(v -> {
            edt_storeName.setText(oldName);
            edt_storeAddress.setText(oldAddress);

            edt_storeName.setEnabled(false);
            edt_storeAddress.setEnabled(false);
            btn_store_done.setVisibility(View.GONE);
            btn_store_exit.setVisibility(View.GONE);
            btn_store_edit.setVisibility(View.VISIBLE);
            btn_store_delete.setVisibility(View.VISIBLE);
        });
        // khi insert store
        btn_store_insert.setOnClickListener(v -> {
            CloudinaryManager.getInstance().upload(selectedImageUri).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    Log.d(TAG, "onStart: " + "started");
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    Log.d(TAG, "onStart: " + "uploading");
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    cloudinaryPath = (String) resultData.get("url");
                    Log.d(TAG, "onStart: " + "success");
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: " + error);
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: " + error);
                }
            }).dispatch();

            String storeName = edt_storeName.getText().toString().trim();
            String storeAddress = edt_storeAddress.getText().toString().trim();
            int selectedPosition = spinnerOwner.getSelectedItemPosition();

            User selectedUser = new User();
            if (selectedPosition != Spinner.INVALID_POSITION) {
                selectedUser = sellers.get(selectedPosition);
                int ownerId = selectedUser.getId();
            }
            String storeImage = cloudinaryPath != null ? cloudinaryPath : null;

            if (storeName.isEmpty() || storeAddress.isEmpty() || storeImage == null) {
                Toast.makeText(this.getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                StoreDAO storeDAO = new StoreDAO(this.getContext());
                storeDAO.open();
                Store store = new Store(storeName, storeAddress, storeImage, selectedUser);
                boolean result = storeDAO.insertStore(store);
                if (result) {
                    Toast.makeText(this.getContext(), "Thêm cửa hàng thành công", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_layout, new AdminDashboardFragment())
                            .addToBackStack(null)
                            .commit();

                } else {
                    Toast.makeText(this.getContext(), "Thêm cửa hàng thất bại", Toast.LENGTH_SHORT).show();
                }

            }

        });



        return view;
    }

    private void requestPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){ //Android 13+
            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
                selectImage();
            } else {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                        Manifest.permission.READ_MEDIA_IMAGES
                }, PICK_IMAGE_REQUEST);
            }
        } else { //Android 12 và thấp hơn
            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                selectImage();
            } else {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, PICK_IMAGE_REQUEST);
            }
        }
    }


    // select the image from the gallery
    private void selectImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(i);
    }

    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        selectedImageUri=data.getData();
                        Picasso.get().load(selectedImageUri).into(imgView_storeImage);
                        imgView_storeImage.setVisibility(View.VISIBLE);
                    }
                }
            }
    );
}