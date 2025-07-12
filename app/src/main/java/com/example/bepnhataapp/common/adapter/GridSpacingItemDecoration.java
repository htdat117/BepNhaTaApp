package com.example.bepnhataapp.common.adapter;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Item decoration for RecyclerView grids that adds proper spacing between items
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacingPx, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacingPx;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position < 0) return;

        // Check if it's a header item
        if (parent.getAdapter() instanceof RecommendedAdapter) {
            RecommendedAdapter adapter = (RecommendedAdapter) parent.getAdapter();
            if (adapter.getItemViewType(position) == 0) { // Header
                outRect.left = 0;
                outRect.right = 0;
                outRect.top = spacing;
                outRect.bottom = spacing / 2;
                return;
            }
        }

        // For grid items
        int column = position % spanCount;

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount;
            outRect.right = (column + 1) * spacing / spanCount;
            outRect.top = spacing;
            outRect.bottom = spacing;
        } else {
            outRect.left = column * spacing / spanCount;
            outRect.right = spacing - (column + 1) * spacing / spanCount;
            outRect.top = spacing;
            outRect.bottom = spacing;
        }
    }
} 
