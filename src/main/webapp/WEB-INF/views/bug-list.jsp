<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bug Logging System</title>

    <!-- Simple styling just to make it readable -->
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        h1, h2 {
            margin-bottom: 10px;
        }

        form {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-top: 8px;
        }

        input[type="text"], textarea, select {
            width: 300px;
            padding: 5px;
            margin-top: 4px;
        }

        button {
            margin-top: 10px;
            padding: 6px 12px;
            cursor: pointer;
        }

        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 15px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 6px 8px;
            text-align: left;
        }

        th {
            background-color: #f5f5f5;
        }

        .error {
            color: red;
            margin-top: 8px;
        }

        .success {
            color: green;
            margin-top: 8px;
        }

        .filter-container {
            margin-top: 20px;
            margin-bottom: 10px;
        }
    </style>

    <!-- jQuery from CDN -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

    <!-- Expose API base URL from Spring model -->
    <script type="text/javascript">
        var API_BASE_URL = '${apiBaseUrl}';
    </script>
</head>
<body>

<h1>Bug Logging System</h1>

<h2>Report a Bug</h2>
<form id="bugForm">
    <label for="title">Title</label>
    <input type="text" id="title" name="title" required/>

    <label for="description">Description</label>
    <textarea id="description" name="description" rows="3" required></textarea>

    <label for="severity">Severity</label>
    <select id="severity" name="severity" required>
        <option value="LOW">LOW</option>
        <option value="MEDIUM">MEDIUM</option>
        <option value="HIGH">HIGH</option>
        <option value="CRITICAL">CRITICAL</option>
    </select>

    <label for="status">Status</label>
    <select id="status" name="status" required>
        <option value="OPEN">OPEN</option>
        <option value="IN_PROGRESS">IN_PROGRESS</option>
        <option value="RESOLVED">RESOLVED</option>
        <option value="CLOSED">CLOSED</option>
    </select>

    <br/>
    <button type="submit" id="submitBtn">Submit Bug</button>

    <div id="error-messages" style="color:red; margin-bottom:10px;"></div>
    <div id="message" class=""></div>
</form>


<div class="filter-container">
    <label for="severityFilter"><strong>Filter by severity:</strong></label>
    <select id="severityFilter">
        <option value="ALL">All</option>
        <option value="LOW">LOW</option>
        <option value="MEDIUM">MEDIUM</option>
        <option value="HIGH">HIGH</option>
        <option value="CRITICAL">CRITICAL</option>
    </select>
</div>

<h2>Bug List</h2>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Description</th>
        <th>Severity</th>
        <th>Status</th>
        <th>Created Date</th>
    </tr>
    </thead>
    <tbody id="bugs-table-body">
    <!-- Rows will be inserted by jQuery -->
    </tbody>
</table>

<script type="text/javascript">
    // Helper: render bugs into the table
    function renderBugsTable(bugs) {
        var $tbody = $('#bugs-table-body');
        $tbody.empty();

        if (!bugs || bugs.length === 0) {
            var emptyRow = '<tr><td colspan="6">No bugs found.</td></tr>';
            $tbody.append(emptyRow);
            return;
        }

        bugs.forEach(function (bug) {
            var rowHtml =
                '<tr>' +
                '<td>' + (bug.id || '') + '</td>' +
                '<td>' + (bug.title || '') + '</td>' +
                '<td>' + (bug.description || '') + '</td>' +
                '<td>' + (bug.severity || '') + '</td>' +
                '<td>' + (bug.status || '') + '</td>' +
                '<td>' + (bug.createdDate || '') + '</td>' +
                '</tr>';
            $tbody.append(rowHtml);
        });
    }

    // Load bugs via GET /api/bugs[?severity=...]
    function loadBugs() {
        var selectedSeverity = $('#severityFilter').val();
        var url = API_BASE_URL + '/api/bugs';

        if (selectedSeverity && selectedSeverity !== 'ALL') {
            url += '?severity=' + encodeURIComponent(selectedSeverity);
        }

        $.getJSON(url)
            .done(function (data) {
                renderBugsTable(data);
            })
            .fail(function (jqXHR) {
                console.error('Failed to load bugs', jqXHR);
                renderBugsTable([]);
            });
    }

    $(document).ready(function () {
        var $message = $('#message');

        // Initial load
        loadBugs();

        // Handle form submit: POST /api/bugs
    $("#bugForm").submit(function (e) {
        e.preventDefault();

        $("#submitBtn").prop("disabled", true).text("Saving...");

        $.ajax({
            url: API_BASE_URL + "/api/bugs",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                title: $("#title").val(),
                description: $("#description").val(),
                severity: $("#severity").val(),
                status: $("#status").val()
                }),
            success: function () {
                $("#error-messages").empty();
                loadBugs();
                $("#bugForm")[0].reset();
                },
            error: function (xhr) {
                const errors = xhr.responseJSON;
                let html = "<ul>";
                for (const field in errors) {
                    html += "<li>" + errors[field] + "</li>";
                    }
                html += "</ul>";
                $("#error-messages").html(html);
                },
            complete: function () {
                $("#submitBtn").prop("disabled", false).text("Submit Bug");
                }
            });
        });

        // Handle severity filter change
        $('#severityFilter').on('change', function () {
            loadBugs();
        });
    });

</script>

</body>
</html>
