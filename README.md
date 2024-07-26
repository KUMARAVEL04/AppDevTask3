Merit Match

Khadeer and Akshay want to help others and get help with small tasks in their community. They envision an app where users can earn and spend "karma points" to assist each other. Can you help them build this app?
App Logic

Users can browse a list of tasks posted by others in their local area and completing a task earns them karma points, transferred from the person who posted the task. Users can post tasks they need help with, specifying the number of karma points they're willing to offer from their account.
Modes

Normal Mode - Completed Fully

1.  Create a backend server to handle app requests and set up a database with tables to store user information, tasks, and karma point transactions. (Implemented Using FAST Api)
2.  The app should have Signup and Login pages authenticated by the backend server, giving them credit of certain karma points upon Signup. (Register is restricted to once per device, subsequents restarts are redirected to the login page)
3.  Fetch and display user details on the home page from the backend server. (Restricted to basic details like karma, username, tasks they have posted for help)
4.  Allow users to post tasks they need help with, specifying the number of karma points they're willing to offer.  (Implemented a page for creating tasks)
5.  Display a list of tasks available for each user to help others with. They should be able to reserve tasks so that others don’t start doing it simultaneously.
6.  The reserved transaction should be settled once the seeker approves of the task completion. (Implemented along with completion of task)

Hacker Mode - Completed Ones:

1.  Allow help seekers to edit a task from their history of posts which are yet to be completed. (If a task is reserved, the user is notified that there is a change)
2.  Provide an alternative way for users to acquire more karma points when needed. (An Easter egg is implemented where upon pressing the karma circl a specific amount of times, you get an extra karma point)
3.  Display a history of tasks helped and sought by the user with the help of Recycler View. (Primitive version of the non-recycler compose is implemented)
    
Not-Completed:
1.Implement a reputation system where users can rate each other's experience after completing a task, building trust and accountability. This information can be stored on the backend server.
