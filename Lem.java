/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *    ZeroR.java
 *    Copyright (C) 1999-2012 University of Waikato, Hamilton, New Zealand
 *
 */

package weka.classifiers.rules;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

import java.util.HashMap;
import java.util.Map;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Sourcable;
import weka.core.AdditionalMeasureProducer;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.WeightedInstancesHandler;
import weka.core.pmml.Array;

/*
*
*
* created by ggladko97 (Yevhenii Hladkevych)
*
*
* */
public class Lem extends AbstractClassifier implements
  WeightedInstancesHandler, Sourcable,AdditionalMeasureProducer {

  private Instances m_All;

  private ArrayList<ArrayList<Instance>> m_ClassInstances; // K

  private ArrayList<Attribute> m_Rules = null; //P (p.215 manual)


  /** The class attribute of the data */
  private Attribute m_Class;//class attribute

  public Instances getM_All() {
    return m_All;
  }

  public void setM_All(Instances m_All) {
    this.m_All = m_All;
  }

  public Attribute getM_Class() {
    return m_Class;
  }

  public void setM_Class(Attribute m_Class) {
    this.m_Class = m_Class;
  }

  @Override
  public void buildClassifier(Instances data) throws Exception {
    Instances instances = new Instances(data);
    m_All = instances;

    instances.setClass(m_Class);

    Enumeration<Object> enu = instances.classAttribute().enumerateValues();

    ArrayList<ArrayList<Instance>> listOfLists = new ArrayList<>();
    listOfLists = sortByClass(instances);
    m_ClassInstances = listOfLists;

    while (!m_ClassInstances.isEmpty()) {

      for (ArrayList<Instance> listClassInstances : m_ClassInstances) {
        Map<Attribute, ArrayList<HashMap<Instance,Object>>> localCovering = new HashMap<>();//local covering of @param listOfAttributeValues
        Map<Attribute, ArrayList<HashMap<Instance,Object>>> listOfAttributeValues = new HashMap<>();

        for (Instance instance : listClassInstances) {
          int numAttr = instance.numAttributes()-1;//it should be removing classAtt value
          for (int i = 0; i < numAttr; i++) {
            ArrayList<HashMap<Instance,Object>> localList = new ArrayList<>();
            HashMap<Instance, Object> localMap = new HashMap<>();
            localMap.put(instance, instance.attribute(i));
            localList.add(localMap);
            listOfAttributeValues.put(instance.attribute(i), localList);
          }
          while (localCovering.isEmpty() || localCovering.entrySet().containsAll(listOfAttributeValues.entrySet())) {
              /*TODO implement algotithm of choosing best attributes
              *
              * */
              int countOccurences = 0;
              for (Map.Entry<Attribute, ArrayList<HashMap<Instance,Object>>> entry :
                  listOfAttributeValues.entrySet()) {

                ArrayList<HashMap<Instance,Object>> entryValues = entry.getValue();

                for (int i = 0; i < entryValues.size(); i++) {

                  //TODO implement comparison of Objects (depending on their type)

                  Collection<Object> values = entryValues.get(i).values();

                }


              }

           // chooseAttributesLem(listOfAttributeValues);



         }


        }

      }

    }

    // remove instances with missing class
  }



  /*
  * Sorting instances by class
  *
  * @param instances  input set of instances
  *
  * @return List with Lists of instances per each class  *
  *
  *
  * */
  private static ArrayList<ArrayList<Instance>> sortByClass(Instances instances) {
    Enumeration<Object> enu = instances.classAttribute().enumerateValues();
    ArrayList<ArrayList<Instance>> listOfLists = new ArrayList<>();
    ArrayList<Object> enuAL = new ArrayList<>();
    if (enu != null) {
      enuAL = Collections.list(enu);
    }

    for (Object classAttribute : enuAL) {
      //System.out.println(classAttribute.toString());
      ArrayList<Instance> listOfInstancesPerClass = new ArrayList<>();
      for (Instance instance : instances) {
        //System.out.println(instance);
        if (instance.stringValue(instance.classAttribute())
            .equals(String.valueOf(classAttribute))) {
          //System.out.println("i'm here");
          listOfInstancesPerClass.add(instance);
        }
      }
      listOfLists.add(listOfInstancesPerClass);
    }
    return listOfLists;
  }

  @Override
  public Enumeration<String> enumerateMeasures() {
    return null;
  }

  @Override
  public double getMeasure(String measureName) {
    return 0;
  }



  /*
  * sort listOfAttributeValues to format: <Attribute,<Value, Occurenceses>>
  *   @param listOfAttributeValues input set
  *   @return HashMap with occurenceces of each Attribute.value
  *
  * */
  private static HashMap<Attribute, HashMap<Object, Integer>> countOccurences (
      ListMultimap<Attribute, ArrayList<HashMap<Instance, Object>>> listOfAttributeValues) {

    return null;
  }

  /*
  *  searching for best fitting pair <Attribute,<Value, Occurence>>
  *   @param sortedOccs list with <Attribute,<Value, Occurence>> counted
  *   @return one element Hash Map <Attribute,<Value, Occurence>>
  *   if our HashMap will return more then 1 element we should implement another
  *   method as in pseudokod P.dr. Wrzesznia
  *
  * */
  private static HashMap<Attribute, HashMap<Object, Integer>> checkHighestPriority(HashMap<Attribute, HashMap<Object, Integer>> sortedOccs) {
    return null;
  }

  public static void main(String[] argv) {
    try {
      BufferedReader br = new BufferedReader(new FileReader("/home/ggladko97/Downloads/weka-3-8-1/data/iriska.arff"));
      Instances instances = new Instances(br);
      instances.setClass(instances.attribute("class"));
      PrintWriter out = new PrintWriter(new FileWriter("/home/ggladko97/Desktop/log.txt", true), true);
     // out.write(occurences.toString());
     // out.close();
      //System.out.println("INst:"+instances.get(3).classAttribute().value(2));
      //Instances res = new Instances(instances).sort(instances.classAttribute().value(0));

      Enumeration<Object> enu = instances.classAttribute().enumerateValues();

      ArrayList<ArrayList<Instance>> listOfLists = new ArrayList<>();
      listOfLists = sortByClass(instances);
      ArrayList<ArrayList<Instance>> m_ClassInstances = listOfLists;

      //while (!m_ClassInstances.isEmpty()) {

        for (ArrayList<Instance> listClassInstances : m_ClassInstances) {


          ListMultimap<Attribute, ArrayList<Object>> localCovering = ArrayListMultimap.create();//local covering of @param listOfAttributeValues
          ListMultimap<Attribute, ArrayList<HashMap<Instance,Object>>> listOfAttributeValues =
              ArrayListMultimap.create();

          for (Instance instance : listClassInstances) {
            int numAttr = instance.numAttributes()-1;//it should be removing classAtt value
            for (int i = 0; i < numAttr; i++) {
              ArrayList<HashMap<Instance,Object>> localList = new ArrayList<>();
              HashMap<Instance, Object> localMap = new HashMap<>();
              localMap.put(instance, instance.value(i));
              localList.add(localMap);
              listOfAttributeValues.put(instance.attribute(i), localList);

            }


        }
         // System.out.println("List Of Attribute value: " + listOfAttributeValues);

          /*
          * Here we have listOfAttValues which looks like:
          * {<@attribute petalwidth numeric >, <<[{5.1,3.5,1.4,0.2,Iris-setosa=0.2}]>,<[{4.7,3.2,1.3,0.2,Iris-setosa=0.2}]>...>,
          * <@attribute petallength numeric >,<< ....>>,...>         *
          *
          * FOR A PARTICULAR CLASS!!! (we have 3 classes here in our example: iris-setosa,versicolor,virginica)
          * */

          //if local covering is empty or  for example: {1,2,7} from {1,2,3,7}
          while (localCovering.isEmpty() || localCovering.values().containsAll(listOfAttributeValues.values())) {

            //occurenceces: <Pentallength, <1, 10 times>>...
            HashMap<Attribute,HashMap<Object,Integer>> occurences = new HashMap<>();

            //TODO: implemet method
            HashMap<Attribute,HashMap<Object,Integer>> sortedOccs = countOccurences(listOfAttributeValues);

            //TODO: implement method
            HashMap<Attribute,HashMap<Object,Integer>> highestPriority = checkHighestPriority(sortedOccs);

            //add best attribute-value pairs to local covering
            ArrayList<Object> values = new ArrayList<>();
            for (Map.Entry<Attribute, HashMap<Object, Integer>> map : highestPriority.entrySet()) {
              values.add(map.getValue());
              localCovering.put(map.getKey(),values);
            }



            //BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/home/ggladko97/Desktop/log.txt")));
            //writer.write(occurences.toString());






          }





      }


      //Enumeration<Object> enu = instances.classAttribute().enumerateValues();
      //ArrayList<Instance> result = new ArrayList<Instance>();
      //ArrayList<ArrayList<Instance>> listOfLists = new ArrayList<>();
      //ArrayList<Object> enuAL = Collections.list(enu);
      //
      //
      //HashMap<Attribute,Object> attValue = new HashMap<>();
      //Instance instance = instances.get(1);
      //Instance instance1 = instances.get(2);
      //for (int i=0; i<instance.numAttributes()-1;i++) {
      //  attValue.put(instance.attribute(i),instance.value(instance.attribute(i)));
      //  attValue.put(instance1.attribute(i),instance1.value(instance1.attribute(i)));
      //}
      //System.out.println(attValue);

      //
      //for (Object classAttribute : enuAL) {
      //  System.out.println(classAttribute.toString());
      //  ArrayList<Instance> listOfInstancesPerClass = new ArrayList<>();
      //  for (Instance instance : instances) {
      //    System.out.println(instance);
      //    if (instance.stringValue(instance.classAttribute()).equals(String.valueOf(classAttribute))) {
      //      //System.out.println("i'm here");
      //      listOfInstancesPerClass.add(instance);
      //    }
      //  }
      //  listOfLists.add(listOfInstancesPerClass);
      //
      //}
      //
      //System.out.println(listOfLists);

      //for (int i = 0; i < enuAL.size(); i++) {
      //  ArrayList<Instance> listAttribute = new ArrayList<>();
      //  listOfLists.add(listAttribute);
      //  for (int j = 0; j < instances.size(); j++) {
      //    for (int k = 0; k < instances.get(j).classAttribute().numValues(); k++) {
      //      System.out.println("Before check: " + "i:" + i + " j:" + j + " k" + k);
      //      if(instances.get(j).classAttribute().value(k).equals(String.valueOf(enuAL.get(i)))) {
      //        System.out.println("i:" + i + " j:" + j + " k" + k);
      //        System.out.println("Instance: " + instances.get(j).classAttribute().value(k));
      //        System.out.println("Compare with " + String.valueOf(enuAL.get(i)));
      //
      //        listAttribute.add(instances.get(j));
      //        System.out.println(listAttribute);
      //        System.out.println(listOfLists);
      //      }
      //    }
      //  }
      //}
      //System.out.println(listOfLists);
      //

      //while (enu.hasMoreElements()) {
      //  System.out.println("Next elem: + " + enu.nextElement());
      //  for (int i = 0; i < instances.size(); i++) {
      //    for (int j = 0; j < instances.get(i).classAttribute().numValues(); j++) {
      //      System.out.println("I="+i+"J="+j);
      //      if (instances.get(i).classAttribute().value(j).equals(String.valueOf(enu.nextElement()))) {
      //        result.add(instances.get(i));
      //        System.out.println(result);
      //
      //      }
      //
      //    }
      //  }
      //}
      //instances.sort(4);
      //System.out.println("SORT: " + instances);
      //System.out.println("END SORT");
      //
      //System.out.println(instances.get(1));//instance: 4.9,3,1.4,0.2,Iris-setosa
      //System.out.println(instances.get(1).attribute(4).value(2));//Iris-virginica
      //double[] values = new double[instances.numAttributes()];
      //System.out.println(instances.numAttributes());//5
      ////Enumeration<Object> enu = instances.attribute(4).enumerateValues();
      //
      //if(enu != null) {
      //  while(enu.hasMoreElements()) {
      //    System.out.println(enu.nextElement().toString());//Iris-setosa Iris-versicolor Iris-virginica
      //  }
      //}
      //
      //Instance instance1 = instances.get(1);
      //System.out.println("instance1.stringValue: " + instance1.value(2));//that's it, it's our 'decision'
      //System.out.println("instance1.attribute: " + instance1.attribute(2));//.atribute
      //System.out.println("instance1.classValue: " + instance1.classValue());
      //System.out.println("instance1.value: " + instance1.value(4));
      ////
      //instances.stableSort(instances.attribute("class"));
      //
      //Enumeration<Attribute> attribute = instances.enumerateAttributes();
      //int a = 0;
      //while (attribute.hasMoreElements()){
      //  System.out.println(attribute.nextElement().toString());
      //  a++;
      //}

      //for(int i=0; i < instances.size(); i++) {
      //  System.out.println(a);
      //  System.out.println(instances.get(i));
      //
      //  System.out.println("\n" + instances.get(i).attribute(i));
      //}

    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    //runClassifier(new Lem(), argv);
  }



  @Override public String toSource(String className) throws Exception {
    return null;
  }
}
