package com.example.chatapplication;

import com.example.chatapplication.data.response.ChatMessageResponse;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Testing
{
    private List<ChatMessageResponse>list=new ArrayList<ChatMessageResponse>();
    private final EventListener<QuerySnapshot> eventListener = (value, error) ->
    {
      if(error !=null)
      {
          return;
      }
      if(value!=null)
      {
          int count=1;
          for(DocumentChange documentChange:value.getDocumentChanges())
          {

          }

          Collections.sort(list,(obj1,obj2)->obj1.getDateObj().compareTo(obj2.getDateObj()));

      }
    };
}
