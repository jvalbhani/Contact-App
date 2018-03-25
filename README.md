# Contact-App
<p><br />The App Contains some basic functionality listed below :</p>
<ul>
<li>Firebase authentication with Email and Sign In with Google Account.
<ul>
<li>The Email sign will send a verification email after setting up the password</li>
<li>And Sign In with Google Account Works with just a simple click and then user has to select his/her Google Account</li>
</ul>
</li>
</ul>
<ul>
<li>User can create contacts with with name, contact number and location detail.
<ul>
<li>The location is added with the help of Google Place API</li>
</ul>
</li>
</ul>
<ul>
<li>Contacts are stored in firebase database.
<ul>
<li>The Dabase is stored on Clound Storage i.e on Firestore</li>
</ul>
</li>
</ul>
<ul>
<li>User can update, delete and see the list of contacts that he/she has created.
<ul>
<li>User can update the details like contact number, name and can also change the location</li>
</ul>
</li>
</ul>
<ul>
<li>User can click on a button on the contact and can see the location which he/she has added to the contact.</li>
</ul>
<p>&nbsp;</p>
<h2>Assumptions :</h2>
<ul>
<li>If the user didn't add contact location then default location is somewhere in antarctica i.e with latitude = 0 and longitude = 0 (if he click's to check location of contact then the map will point to the specified region)
</li>
<li>At the time of update if user doesn't want to change any detail he can simply click Update</li>
</ul>
