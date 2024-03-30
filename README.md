1. Use the accuracy timer, the 'setExact' method of AlarmManager to set the first timer. Avoid using the 'setRepeating' method because the timing may not be precise (due to optimizations by Google for battery consumption and performance). After processing and sending notifications in the 'alarmReceiver', set the second timer.
2. After the application is closed, it relies on a background Service to provide alarm support. I use a foreground Service to ensure that the process is not terminated.
3. For timing information, I use SharedPreferences to save data.
4. In terms of the user interface, I implement a horizontal scrolling effect for the fitness schedule using RecyclerView along with LinearLayoutManager. Additionally, I use CardView to achieve a card-like appearance with shadows and rounded corners. The UI design can be surely improved.
5. Clicking on an item in the list will trigger a dialog that displays detailed information.

