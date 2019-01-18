package game;

import javafx.scene.Scene;

public class ListNode {
    Scene info;
    ListNode next;
    ListNode(Scene s){
        info = s;
    }
    ListNode(Scene s, ListNode node){
        info = s;
        next = node;
    }
}