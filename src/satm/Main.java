package satm;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;


/**
 * @author William Robert Howerton III
 * @version 29/03/2015
 *
 * Terms:
 *
 * Transaction: a complete traversal from S1 to S12 resulting in valid termination
 * Incomplete: An incomplete transaction.
 * MTBF: Mean Time Between Failures
 *
 * The purpose of this program is to simulate a SATM (Simple Automatic Teller Machine) by using methods to mimic
 * states of the SATM and using the Java Random class to mimic probabilistic transitions between states. The Random
 * class nextDouble() method returns a pseudo-randomly generated double between 0 and 1, so I use this value to
 * determine the next transition by comparing the generated value with the probability of transition. Assuming the
 * nextDouble() distribution is uniform from 0 to 1, this method should produce accurate results.
 *
 * During the course of each individual transaction, state names are appended to the stateTransitions String throughout
 * the course of the program. The Strings are added to a hashSet to keep track of unique transaction paths through the
 * SATM and a hashTable to keep track of number of Strings produced. At the end of execution, each unique transaction
 * String produced is printed to the console along with the number produced. The total number of unique transaction
 * Strings produced is also given.
 *
 * */

class Main {


    private Random r = new Random();
    private static String stateTransitions;
    private int infiniteLoopStopper = 0;
    private HashSet<String> transitionStates = new HashSet<>(60);
    private Hashtable<Integer, Integer> transitionStatesCount = new Hashtable<>(60);



    public static void main(String[] args){

        Main start = new Main();
        start.startTest(500000);


    }

    private void startTest(int numberOfIterations){

        int numberOfUniqueStatesCounter = 0;
        for(int i=0; i < numberOfIterations; i++){
            CardSwipe(false);
            infiniteLoopStopper = 0;
        }
        System.out.printf("%-40s\t%s\n", "---TRANSACTION STATE ROUTE---", "Number of Occurrences");
        for(String s : transitionStates) {
            System.out.printf("%-40s\t%d\n", s, transitionStatesCount.get(s.hashCode()));
            numberOfUniqueStatesCounter++;
        }

        System.out.printf("Total unique States: %d", numberOfUniqueStatesCounter);
    }

    private void CardSwipe(boolean recursiveCall){
        if(!recursiveCall){
            stateTransitions = "S1-";
        }
        else{
            stateTransitions+="S1-";
        }

        double result = r.nextDouble();

        if ((result < .02) && (infiniteLoopStopper == 1)) {
            stateTransitions = "INCOMPLETE";
            CloseSession();
        }

        else if (result < .02) {
            infiniteLoopStopper++;
            CardSwipe(true);
        }

        else{
            FirstPINTry();
        }


    }

    private void FirstPINTry(){
        stateTransitions += "S2-";
        double result = r.nextDouble();
        if (result < .04){
            SecondPINTry();
        }
        else{
            TransactionChoice();
        }
    }

    private void SecondPINTry(){
        stateTransitions += "S3-";
        double result = r.nextDouble();

        if (result<.04){
            ThirdPINTry();
        }
        else{
            TransactionChoice();
        }

    }

    private void ThirdPINTry(){
        stateTransitions += "S4-";

        double result = r.nextDouble();

        if(result<.04){
            stateTransitions = "INCOMPLETE";
            CloseSession();
        }
        else{
            TransactionChoice();
        }
    }

    private void TransactionChoice(){
        stateTransitions += "S5-";
        double result = r.nextDouble();
        boolean tieBreaker = r.nextBoolean();
        if(result<.01){
            LowOnCash();
        }
        else if(result<.02 && tieBreaker){
            Balance();
        }
        else if(result<.02 && !tieBreaker){
            FundsInsufficient();
        }
        else if(result<.05 && tieBreaker){
            Deposit();
        }
        else if(result<.05 && !tieBreaker){
            NotMultipleOfTwenty();
        }
        else{
            ValidWithdrawal();
        }
    }

    private void Balance(){
        stateTransitions += "S6-";
        CloseSession();
    }

    private void Deposit(){
        stateTransitions += "S7-";
        CloseSession();
    }

    private void ValidWithdrawal(){
        stateTransitions += "S8-";
        CloseSession();
    }

    private void NotMultipleOfTwenty(){
        stateTransitions += "S9-";
        CloseSession();
    }

    private void FundsInsufficient(){
        stateTransitions += "S10-";
        CloseSession();
    }

    private void LowOnCash(){
        stateTransitions += "S11-";
        CloseSession();
    }

    private void CloseSession(){
        if(!(stateTransitions.equalsIgnoreCase("INCOMPLETE"))){
            stateTransitions += "S12";
        }

        if(transitionStates.contains(stateTransitions)){
            int counter = transitionStatesCount.get(stateTransitions.hashCode());
            counter++;
            transitionStatesCount.put(stateTransitions.hashCode(),counter);
        }
        else{
            transitionStatesCount.put(stateTransitions.hashCode(), 1);
            transitionStates.add(stateTransitions);
        }
    }
}
