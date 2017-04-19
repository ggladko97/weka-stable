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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Sourcable;
import weka.core.AbstractInstance;
import weka.core.AdditionalMeasureProducer;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionUtils;
import weka.core.Utils;
import weka.core.WeightedInstancesHandler;

/**
 * <!-- globalinfo-start --> Class for building and using a 0-R classifier.
 * Predicts the mean (for a numeric class) or the mode (for a nominal class).
 * <p/>
 * <!-- globalinfo-end -->
 * 
 * <!-- options-start --> Valid options are:
 * <p/>
 * 
 * <pre>
 * -D
 *  If set, classifier is run in debug mode and
 *  may output additional info to the console
 * </pre>
 * 
 * <!-- options-end -->
 * 
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @version $Revision: 12024 $
 */
public class Lem extends AbstractClassifier implements
  WeightedInstancesHandler, Sourcable,AdditionalMeasureProducer {

  private Instances m_All;

  private Instances m_ClassInstances; // K

  private ArrayList<Attribute> m_Rules = null; //P (p.215 manual)


  /** The class attribute of the data */
  private Attribute m_Class;//class attribute

  @Override
  public void buildClassifier(Instances data) throws Exception {
    Instances instances = new Instances(data);
    m_All = instances;

    instances.setClass(m_Class);

    ArrayList<ArrayList<Instance>> listOfLists = new ArrayList<>();

    Enumeration<Object> listOfClassAtt = instances.classAttribute().enumerateValues();
    if (listOfClassAtt != null) {
      while (listOfClassAtt.hasMoreElements()) {
        sortByClass();

        for (int i = 0; i < m_All.size(); i++) {
          for (int j = 0; j < m_All.get(i).classAttribute().numValues(); j++) {
            if(m_All.get(i).classAttribute().value(j) == listOfClassAtt.nextElement()) {
              //it's not good I know
              m_ClassInstances.add(m_All.get(i));
            }
          }
        }

      }

    } else {
      //TODO deal with null pointer
    }

    // remove instances with missing class
  }

  private void sortByClass() {
  }

  @Override
  public Enumeration<String> enumerateMeasures() {
    return null;
  }

  @Override
  public double getMeasure(String measureName) {
    return 0;
  }

  public static void main(String[] argv) {
    try {
      BufferedReader br = new BufferedReader(new FileReader("/home/ggladko97/Downloads/weka-3-8-1/data/iriska.arff"));
      Instances instances = new Instances(br);
      instances.setClass(instances.attribute("class"));
      //System.out.println("INst:"+instances.get(3).classAttribute().value(2));
      //Instances res = new Instances(instances).sort(instances.classAttribute().value(0));
      Enumeration<Object> enu = instances.classAttribute().enumerateValues();
      ArrayList<Instance> result = new ArrayList<Instance>();
      ArrayList<Object> enuAL = Collections.list(enu);
      for (int i = 0; i < enuAL.size(); i++) {
        for (int j = 0; j < instances.size(); j++) {
          for (int k = 0; k < instances.get(j).classAttribute().numValues(); k++) {
            System.out.println("Before check: " + "i:" + i + " j:" + j + " k" + k);
            if(instances.get(j).classAttribute().value(k).equals(String.valueOf(enuAL.get(i)))) {
              System.out.println("i:" + i + " j:" + j + " k" + k);
              System.out.println("Instance: " + instances.get(j).classAttribute().value(k));
              System.out.println("Compare with " + String.valueOf(enuAL.get(i)));
              result.add(instances.get(j));
              System.out.println(result);
            }
          }
        }
      }
      System.out.println(result);


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
