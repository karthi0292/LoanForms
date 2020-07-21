# LoanForms
Dynamic forms will be created based on Json Configuration

Flow

1)This demo app contains forms where we have form to apply for loan

2)It has 3 details
 i)Personal Details
 ii)Indentification Details
 iii)Loan Details
 
3.Each form have some fields related to personal and loan 

Technical

1)Views are created dynamically based on Json Configuration

2)Applied Android Navigation Fragment concept where single fragment is reused for 3 forms.

3)Each field is validated by conditions which retrieved from Json

4)Passing key value pair of selected items when moving from one form to another form.Finally data's are placed in the last form.

5)Safe Args Gradle plugin used for passing arguments safely 

6)Easily we can add components by adding fields in Json Configuration
