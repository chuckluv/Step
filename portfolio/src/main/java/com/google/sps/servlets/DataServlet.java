// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Post;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/Comments")
public class DataServlet extends HttpServlet {
  private static final String COMMENTS_ENTITY_NAME = "Comments";
  private static final String TIMESTAMP_PROPERTY_KEY = "timestamp";
  private static final String COMMENT_PROPERTY_KEY = "comment";
  private static final String EMAIL_PROPERTY_KEY = "Email";
  private static final String COMMENT_PARAMETER_NAME = "comment";
  private static final String MAX_COMMENTS_PARAMETER_NAME = "maxnum";
  private static final int DEFAULT_MAX_COMMENTS = 5;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query =
        new Query(COMMENTS_ENTITY_NAME).addSort(TIMESTAMP_PROPERTY_KEY, SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    String maxString = request.getParameter(MAX_COMMENTS_PARAMETER_NAME);
    int max = DEFAULT_MAX_COMMENTS;
    if (maxString != null) {
      try {
        max = Integer.parseInt(maxString);

      } catch (NumberFormatException e) {
        System.out.println("Could not convert to int|" + maxString + "|");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().printf("%s Must be a integer", MAX_COMMENTS_PARAMETER_NAME);
        return;
      }
      }
    List<Post> comments = new ArrayList<>();
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(max);
    for (Entity entity : results.asIterable(fetchOptions)) {
      long id = entity.getKey().getId();
      String comment = (String) entity.getProperty(COMMENT_PROPERTY_KEY);
      long timestamp = (long) entity.getProperty(TIMESTAMP_PROPERTY_KEY);
      String email = (String) entity.getProperty(EMAIL_PROPERTY_KEY);
      Post post = new Post(id, comment, timestamp, email);
      comments.add(post);
    }
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userComment = request.getParameter(COMMENT_PARAMETER_NAME);
    long timestamp = System.currentTimeMillis();
    UserService userService = UserServiceFactory.getUserService();
    String userEmail = userService.getCurrentUser().getEmail();
    String maxString = request.getParameter(MAX_COMMENTS_PARAMETER_NAME);
    if (userComment != null) {
      Entity taskEntity = new Entity(COMMENTS_ENTITY_NAME);
      taskEntity.setProperty(COMMENT_PROPERTY_KEY, userComment);
      taskEntity.setProperty(TIMESTAMP_PROPERTY_KEY, timestamp);
      taskEntity.setProperty(EMAIL_PROPERTY_KEY, userEmail);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(taskEntity);
    }

    response.sendRedirect("/index.html");
  }
}
