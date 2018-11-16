/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;


public class ArrayListMatrix {
    
    public static ArrayList<ArrayList<Integer>> copyArrayList(ArrayList<ArrayList<Integer>> input) {
        ArrayList<ArrayList<Integer>> returnArray = new ArrayList<>();
        ArrayList<Integer> aux;
        for (int i = 0; i < input.size(); i++) {
            aux = new ArrayList();
            for (int j = 0; j < input.get(i).size(); j++) {
                aux.add(input.get(i).get(j));
            }
            returnArray.add(aux);
        }
        return returnArray;
    }
}
