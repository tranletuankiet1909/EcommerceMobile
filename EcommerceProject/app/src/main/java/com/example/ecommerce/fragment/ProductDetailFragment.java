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

import android.text.InputType;
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
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.ecommerce.CloudinaryManager;
import com.example.ecommerce.R;
import com.example.ecommerce.dao.CategoryDAO;
import com.example.ecommerce.dao.ProductDAO;
import com.example.ecommerce.dao.StoreDAO;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.Store;
import com.example.ecommerce.entity.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends Fragment {
    private ImageView imgView_produt_image;
    private EditText edt_productName, edt_productDescription, edt_productPrice, edt_productQuantity;
    private Spinner spinner_categoryInProductDetail, spinner_storeInProductDetail;
    private Button btn_productExit, btn_productDone, btn_productInsert, btn_productEdit, btn_productDelete, btn_insertImageProduct;
    private String cloudinaryPath;
    private Uri selectedImageUri;
    private static final String TAG = "Upload ###";
    private static final int PICK_IMAGE_REQUEST = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailFragment newInstance(String param1, String param2) {
        ProductDetailFragment fragment = new ProductDetailFragment();
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
                        Picasso.get().load(selectedImageUri).into(imgView_produt_image);
                        imgView_produt_image.setVisibility(View.VISIBLE);
                    }
                }
            }
    );
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
    private void initConfig() {
        CloudinaryManager.init(this.getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initConfig();
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        imgView_produt_image = view.findViewById(R.id.imgView_product_image);
        edt_productName = view.findViewById(R.id.edt_product_name);
        edt_productDescription = view.findViewById(R.id.edt_product_description);
        edt_productPrice = view.findViewById(R.id.edt_product_price);
        edt_productQuantity = view.findViewById(R.id.edt_product_quantity);
        spinner_categoryInProductDetail = view.findViewById(R.id.spinner_category_in_productDetail);
        spinner_storeInProductDetail = view.findViewById(R.id.spinner_store_in_productDetail);
        btn_productDone = view.findViewById(R.id.btn_product_done);
        btn_productInsert = view.findViewById(R.id.btn_product_insert);
        btn_productEdit = view.findViewById(R.id.btn_product_edit);
        btn_productExit = view.findViewById(R.id.btn_product_exit);
        btn_productDelete = view.findViewById(R.id.btn_product_delete);
        btn_insertImageProduct = view.findViewById(R.id.btn_insert_productImage);

        edt_productPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt_productQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);

        btn_insertImageProduct.setOnClickListener(v -> {
            requestPermissions();
        });

        // load data for spinner
        CategoryDAO categoryDAO = new CategoryDAO(this.getContext());
        StoreDAO storeDAO = new StoreDAO(this.getContext());
        categoryDAO.open();
        storeDAO.open();

        List<Category> categories = categoryDAO.getCategories();
        List<Store> stores = storeDAO.getStores();
        List<String> cateNames = new ArrayList<>();
        List<String> storeNames = new ArrayList<>();

        categoryDAO.close();
        storeDAO.close();

        for (Category c: categories) {
            cateNames.add(c.getName());
        }
        for (Store s: stores) {
            storeNames.add(s.getName());
        }

        ArrayAdapter<String> cAdapter = new ArrayAdapter<>(
            this.getContext(),
            android.R.layout.simple_spinner_item,
            cateNames
        );

        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(
            this.getContext(),
            android.R.layout.simple_spinner_item,
            storeNames
        );

        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_categoryInProductDetail.setAdapter(cAdapter);
        spinner_storeInProductDetail.setAdapter(sAdapter);

        //click vao product item
        Bundle bundleProduct = getArguments();
        Product p;
        if (bundleProduct != null) {
            p = (Product) bundleProduct.getSerializable("product");
            String productName = p.getName();
            String productImage = p.getImage();
            String productDescription = p.getDescription();
            String productPrice = p.getPrice();
            String productQuantity = String.valueOf(p.getQuantity());
            Category cate = p.getCategory();
            Store store = p.getStore();

            Glide.with(requireContext()).load(productImage).into(imgView_produt_image);
            edt_productName.setText(productName);
            edt_productDescription.setText(productDescription);
            edt_productQuantity.setText(productQuantity);
            edt_productPrice.setText(productPrice);
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId() == cate.getId()) {
                    spinner_categoryInProductDetail.setSelection(i);
                }
            }
            for (int i = 0; i < stores.size(); i++) {
                if (stores.get(i).getId() == store.getId()) {
                    spinner_storeInProductDetail.setSelection(i);
                }
            }
            btn_productInsert.setVisibility(View.GONE);
            btn_productEdit.setVisibility(View.VISIBLE);
            btn_productDelete.setVisibility(View.VISIBLE);
            btn_insertImageProduct.setVisibility(View.GONE);
            edt_productName.setEnabled(false);
            edt_productDescription.setEnabled(false);
            edt_productQuantity.setEnabled(false);
            edt_productPrice.setEnabled(false);
            spinner_storeInProductDetail.setEnabled(false);
            spinner_categoryInProductDetail.setEnabled(false);
        } else {
            p = new Product();
        }

        // Set event click button edit
        btn_productEdit.setOnClickListener(v -> {
            edt_productName.setEnabled(true);
            edt_productDescription.setEnabled(true);
            edt_productQuantity.setEnabled(true);
            edt_productPrice.setEnabled(true);

            btn_productEdit.setVisibility(View.GONE);
            btn_productDelete.setVisibility(View.GONE);
            btn_productDone.setVisibility(View.VISIBLE);
            btn_productExit.setVisibility(View.VISIBLE);
        });

        // set event click done
        btn_productDone.setOnClickListener(v -> {
            String productName = edt_productName.getText().toString();
            String productDescription = edt_productDescription.getText().toString();
            String productPrice = edt_productPrice.getText().toString();
            int productQuantity = Integer.parseInt(edt_productQuantity.getText().toString());


            Category category = new Category();
            Store store = new Store();
            String productImage = "";
            int productId = 0;
            if (bundleProduct != null) {
                productId = p.getId();
                category = p.getCategory();
                store = p.getStore();
                productImage = p.getImage();
            }
            Product product = new Product(productId, productName, productImage, productDescription, productPrice, productQuantity, category, store);
            if (productName.isEmpty() || productDescription.isEmpty() || productPrice.isEmpty() || productImage.isEmpty()) {
                Toast.makeText(this.getContext(), "Vui lòng không để trống thông tin", Toast.LENGTH_SHORT).show();
            } else {
                ProductDAO productDAO = new ProductDAO(this.getContext());
                productDAO.open();
                boolean result = productDAO.updateProduct(product);
                productDAO.close();
                if (result) {
                    edt_productName.setText(productName);
                    edt_productDescription.setText(productDescription);
                    edt_productQuantity.setText(String.valueOf(productQuantity));
                    edt_productPrice.setText(productPrice);

                    edt_productName.setEnabled(false);
                    edt_productDescription.setEnabled(false);
                    edt_productQuantity.setEnabled(false);
                    edt_productPrice.setEnabled(false);

                    btn_productDone.setVisibility(View.GONE);
                    btn_productExit.setVisibility(View.GONE);
                    btn_productEdit.setVisibility(View.VISIBLE);
                    btn_productDelete.setVisibility(View.VISIBLE);

                    Toast.makeText(this.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.getContext(), "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // set event click exit
        String oldName = edt_productName.getText().toString().trim();
        String oldDescription = edt_productDescription.getText().toString().trim();
        String oldPrice = edt_productPrice.getText().toString().trim();
        int oldQuanity = Integer.parseInt(edt_productQuantity.getText().toString().trim());
        btn_productExit.setOnClickListener(v -> {
            edt_productName.setText(oldName);
            edt_productDescription.setText(oldDescription);
            edt_productPrice.setText(oldPrice);
            edt_productQuantity.setText(String.valueOf(oldQuanity));

            edt_productName.setEnabled(false);
            edt_productDescription.setEnabled(false);
            edt_productQuantity.setEnabled(false);
            edt_productPrice.setEnabled(false);

            btn_productDone.setVisibility(View.GONE);
            btn_productExit.setVisibility(View.GONE);
            btn_productEdit.setVisibility(View.VISIBLE);
            btn_productDelete.setVisibility(View.VISIBLE);
        });
        //khi insert product
        btn_productInsert.setOnClickListener(v -> {
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

            String productName = edt_productName.getText().toString().trim();
            String productDescription = edt_productDescription.getText().toString().trim();
            String productPrice = edt_productPrice.getText().toString().trim();
            int productQuantity = Integer.parseInt(edt_productQuantity.getText().toString().trim());

            int categorySelectedPosition = spinner_categoryInProductDetail.getSelectedItemPosition();
            int storeSelectedPosition = spinner_storeInProductDetail.getSelectedItemPosition();

            Category selectedCategory = new Category();
            if (categorySelectedPosition != Spinner.INVALID_POSITION) {
                selectedCategory = categories.get(categorySelectedPosition);
//                int cateId = selectedCategory.getId();
            }

            Store selectedStore = new Store();
            if (storeSelectedPosition != Spinner.INVALID_POSITION) {
                selectedStore = stores.get(storeSelectedPosition);
//                int storeId = selectedStore.getId();
            }
            String productImage = cloudinaryPath != null ? cloudinaryPath : null;

            if (productName.isEmpty() || productDescription.isEmpty() || productPrice.isEmpty() || productImage == null) {
                Toast.makeText(this.getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                ProductDAO productDAO = new ProductDAO(this.getContext());
                productDAO.open();
                Product product = new Product(productName, productImage, productDescription, productPrice, productQuantity, selectedCategory, selectedStore);
                boolean result = productDAO.insertProduct(product);
                productDAO.close();
                if (result) {
                    Toast.makeText(this.getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_layout, new AdminDashboardFragment())
                            .addToBackStack(null)
                            .commit();

                } else {
                    Toast.makeText(this.getContext(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}