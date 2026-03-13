package com.flightbooking.common.util;

public class JwtUtil {
}
//
//public class Solution{
//    public int totalSubarrayCount(int[] arr, int k){
//        if(arr==null || arr.length==0 ) return 0;// since array is positive
//        //States
//        if(k<=1) return 0;
//        int left=0, product=1, totalSubarrayCount=0;
//
//        for(int right=0; right<arr.length;right++){
//            product*=arr[right];
//
//            //Invalid
//            while(product>=k){
//                //update sum with -> remove left element first
//                product/=arr[left];
//                //move left forward
//                left++;
//            }
//            totalSubarrayCount+=(right-left+1);
//        }
//        return totalSubarrayCount;
//    }
//
//    /*
//    * 🟢 5️⃣ Counting – Exactly K (Using atMost Trick)
//    🔹 Subarrays with Exactly K Distinct
//        nums = [1,2,1,2,3], k = 2
//        Output: 7
//**/
//
//    public int subarraysWithExaclyKDistinct(int[] nums, int k ){
//        return subarraysWithAtMostKDistinct(nums,k) - subarraysWithAtMostKDistinct(nums,k-1);
//    }
//    public int subarraysWithAtMostKDistinct(int[] nums, int k ){
//        //States
//        int left=0, totalSubarrayCuont=0;
//        Set<Integer> distinct = new HashSet<>();
//
//        //Expansion->
//        for(int right =0; right < nums.length; right++){
//            distinct.add(nums[right]);
//
//            //Invalid-> shrink window till valid
//            while(distinct.size()>k){
//                distinct.remove(nums[left]);
//                left++;
//            }
//            //Update answer
//            totalSubarrayCuont+=(right-left+1);
//        }
//        return totalSubarrayCuont;
//    }
//
//    /*
//🔹 Count Subarrays with Sum Exactly K (Binary Array)
//nums = [1,0,1,0,1], goal = 2
//Output: 4
//
//(using atMost(goal) - atMost(goal-1))
//    * */
//}
