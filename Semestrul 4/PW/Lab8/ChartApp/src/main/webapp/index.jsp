<%@ taglib uri="http://web2024/chartapp/chart" prefix="ct" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chart</title>
</head>
<body>
<h1>Custom JSP Chart Tag</h1>
<ct:chart
        oxValues="1,2,3,4,5"
        oyValues="10,20,30,40,50"
        oxLabel="Time"
        oyLabel="Quantity"
        oxMin="0"
        oxMax="6"
        oyMin="0"
        oyMax="60"
        color="#FF0000"
        imageUrl="https://miro.medium.com/v2/resize:fit:1131/1*SD4MtJcsheHu5uqH10eyjw.png"/>
</body>
</html>
