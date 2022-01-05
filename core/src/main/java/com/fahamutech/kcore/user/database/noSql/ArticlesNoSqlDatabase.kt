package com.fahamutech.kcore.user.database.noSql;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fahamutech.kcore.user.adapter.ArtAdapter;
import com.fahamutech.kcore.user.database.connector.ArticleDataSource;
import com.fahamutech.kcore.user.model.Article;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.Query;

import java.util.List;

public class ArticlesNoSqlDatabase extends NoSqlDatabase implements ArticleDataSource {

    public ArticlesNoSqlDatabase(Context context) {
        super(context);
    }

    @Override
    public void getArticles(String categoryId, RecyclerView recyclerView,
                            SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setRefreshing(true);
        firestore.collection(NoSqlColl.ARTICLES.name())
                .whereEqualTo("categoryId", categoryId)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Article> articles = queryDocumentSnapshots.toObjects(Article.class);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new ArtAdapter(articles, context));
                    swipeRefreshLayout.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    Snackbar.make(swipeRefreshLayout, "Failed to get articles", Snackbar.LENGTH_SHORT).show();
                    Log.e("TAG ARTICLES", String.valueOf(e));
                    swipeRefreshLayout.setRefreshing(false);
                });
    }
}