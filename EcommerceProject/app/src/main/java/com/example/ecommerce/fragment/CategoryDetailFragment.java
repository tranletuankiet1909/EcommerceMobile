package com.example.ecommerce.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.dao.CategoryDAO;
import com.example.ecommerce.entity.Category;

import java.util.Optional;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryDetailFragment extends Fragment {
    private EditText edtCategoryName;
    private Button btnInsertCategory, btnDoneCategory,
            btnDeleteCategory, btnExitCategory, btnEditCategory;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryDetailFragment newInstance(String param1, String param2) {
        CategoryDetailFragment fragment = new CategoryDetailFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_detail, container, false);
        CategoryDAO categoryDAO = new CategoryDAO(this.getContext());
        edtCategoryName = view.findViewById(R.id.edt_category_name);
        btnInsertCategory = view.findViewById(R.id.btn_category_insert);
        btnDoneCategory = view.findViewById(R.id.btn_category_done);
        btnDeleteCategory = view.findViewById(R.id.btn_category_delete);
        btnEditCategory = view.findViewById(R.id.btn_category_edit);
        btnExitCategory = view.findViewById(R.id.btn_category_exit);

        btnInsertCategory.setOnClickListener(v -> {
            if (edtCategoryName.getText().toString().equals("")) {
                Toast.makeText(this.getContext(), "Vui lòng nhập thông tin đầy đủ", Toast.LENGTH_SHORT).show();
            } else {
                String cateName = edtCategoryName.getText().toString();
                Category category = new Category(cateName);
                categoryDAO.open();
                boolean result = categoryDAO.insertCategory(category);
                if (result) {
                    Toast.makeText(this.getContext(), "Tạo thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.getContext(), "Tạo thất bại", Toast.LENGTH_SHORT).show();

                }
                categoryDAO.close();
            }
        });

        Bundle bundleCate = getArguments();
        if (bundleCate != null) {
            String cateName = bundleCate.getString("cateName");

            edtCategoryName.setText(cateName);

            edtCategoryName.setEnabled(false);
            btnInsertCategory.setVisibility(View.GONE);
            btnEditCategory.setVisibility(View.VISIBLE);
            btnDeleteCategory.setVisibility(View.VISIBLE);
        }

        // Trạng thái khi chọn chỉnh sửa
        btnEditCategory.setOnClickListener(v -> {
            btnEditCategory.setVisibility(View.GONE);
            btnDeleteCategory.setVisibility(View.GONE);
            btnDoneCategory.setVisibility(View.VISIBLE);
            btnExitCategory.setVisibility(View.VISIBLE);

            edtCategoryName.setEnabled(true);
        });

        // Trạng thái khi hoàn tất chỉnh sửa
        btnDoneCategory.setOnClickListener(v -> {
            String cateName = edtCategoryName.getText().toString();
            int cateId = bundleCate.getInt("cateId");

            Category c = new Category(cateId, cateName);
            if (cateName.equals("")) {
                Toast.makeText(this.getContext(), "Vui lòng không để trống thông tin", Toast.LENGTH_SHORT).show();
            } else {
                CategoryDAO cateDAO = new CategoryDAO(this.getContext());
                cateDAO.open();
                boolean result = cateDAO.updateCategory(c);
                if (result) {
                    edtCategoryName.setText(cateName);
                    edtCategoryName.setEnabled(false);
                    btnDoneCategory.setVisibility(View.GONE);
                    btnExitCategory.setVisibility(View.GONE);
                    btnEditCategory.setVisibility(View.VISIBLE);
                    btnDeleteCategory.setVisibility(View.VISIBLE);
                    Toast.makeText(this.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.getContext(), "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Trạng thái thoát khỏi chỉnh sửa
        btnExitCategory.setOnClickListener(v -> {
            edtCategoryName.setText(bundleCate.getString("cateName"));
            edtCategoryName.setEnabled(false);
            btnDoneCategory.setVisibility(View.GONE);
            btnExitCategory.setVisibility(View.GONE);
            btnEditCategory.setVisibility(View.VISIBLE);
            btnDeleteCategory.setVisibility(View.VISIBLE);
        });
        return view;
    }
}