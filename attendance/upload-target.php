<?php
ini_set('auto_detect_line_endings', TRUE);

$dbconn = pg_connect("dbname=attendance_tracker user=attendance password=cs386")
    or die('Could not connect: ' . pg_last_error());
$file = fopen(($_FILES['file']['tmp_name']), 'r');

fgetcsv($file);

//Read in class line
//$_line[0] --> class id 
//$_line[1] --> class name
//$_line[2] --> teacher last
//$_line[3] --> teacher first 
//$_line[4] --> teacher id
$_line = fgetcsv($file);
$class_id = $_line[0];

//Insert instructor (if not already in db)
$query = "select * from teacher where teacher_id='$_line[4]'";
$result = pg_query($query) or die('Query failed: ' . pg_last_error());
if(pg_num_rows($result) == 0) {
	// insert new instructor into table
	$query = "insert into teacher (teacher_last,teacher_first,teacher_id) values('$_line[2]','$_line[3]','$_line[4]')";
    $result = pg_query($query) or die('Query failed: ' . pg_last_error());
}

//Insert new class record into table
$query = "insert into class (class_id,class_name) values('$_line[0]','$_line[1]')";
$result = pg_query($query) or die('Query failed: ' . pg_last_error());

// enroll teacher as owner of class
$query = "insert into teacher_enrollment (teacher_id,class_id) values('$_line[4]','$_line[0]')";
$result = pg_query($query) or die('Query failed: ' . pg_last_error());

//Read in header line (and ignore it)
fgetcsv($file);

//Read in each student line
$num_students = 0;
while (($_line = fgetcsv($file)) !== FALSE) {

	//Insert student (if not already in db)
	//$_line[0] --> student id
	//$_line[1] --> student last
	//$_line[2] --> student first
	$query = "select * from student where student_id='$_line[0]'";
	$result = pg_query($query) or die('Query failed: ' . pg_last_error());
	if(pg_num_rows($result) == 0) {
		// insert new student into table
		$query = "insert into student (student_id,student_last,student_first) values('$_line[0]','$_line[1]','$_line[2]')";
    	$result = pg_query($query) or die('Query failed: ' . pg_last_error());
	}
	
	//Enroll student in the class
	$query = "insert into student_enrollment (student_id,class_id) values('$_line[0]','$class_id')";
    $result = pg_query($query) or die('Query failed: ' . pg_last_error());
	
	$num_students += 1;
}
//print("Class created with $num_students students");

fclose($file);

//print query to attendance website
$query = "select class.class_name,class.class_id, student.student_last, student.student_first, student.student_id, student_enrollment.days_attended from student_enrollment join class on student_enrollment.class_id=class.class_id join student on student.student_id=student_enrollment.student_id where class.class_id='$class_id'";
$result = pg_query($query) or die('Query failed: ' . pg_last_error());
while ($row = pg_fetch_row($result)) print( '<tr><td>'.$row[0].'</td><td>'.$row[1].'</td><td>'.$row[2].'</td><td>'.$row[3].'</td><td>'.$row[4].'</td><td>'.$row[5].'</td>');

pg_close($dbconn);

?>
