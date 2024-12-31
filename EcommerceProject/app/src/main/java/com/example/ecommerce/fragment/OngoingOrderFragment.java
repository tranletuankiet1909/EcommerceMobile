package com.example.ecommerce.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.R;
import com.example.ecommerce.UserManager;
import com.example.ecommerce.adapter.OrderAdapter;
import com.example.ecommerce.dao.OrderDAO;
import com.example.ecommerce.entity.OrderStatus;
import com.example.ecommerce.entity.PurchaseOrder;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OngoingOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OngoingOrderFragment extends Fragment {
    private RecyclerView recyclerView_orderOngoing;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OngoingOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OngoingOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OngoingOrderFragment newInstance(String param1, String param2) {
        OngoingOrderFragment fragment = new OngoingOrderFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ongoing_order, container, false);
        recyclerView_orderOngoing = view.findViewById(R.id.recyclerView_orderOngoing);
        OrderDAO orderDAO = new OrderDAO(this.getContext());
        orderDAO.open();
        List<PurchaseOrder> ongoingOrderList = orderDAO.getOrderByStatusAndUserId(UserManager.getInstance().getUserId(), OrderStatus.ONGOING.getDisplayName());
        OrderAdapter orderAdapter = new OrderAdapter(ongoingOrderList, this.getContext());
        recyclerView_orderOngoing.setAdapter(orderAdapter);
        recyclerView_orderOngoing.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }
}