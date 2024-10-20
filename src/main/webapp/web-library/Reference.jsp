<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
<!-- jQuery for AJAX call -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/web-library/CSS/General.css">
<!-- Bootstrap JS and dependencies -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

<script src="<%=request.getContextPath()%>/web-library/General.js"></script>

<script>
async function encryptPassword(message) {
    const encoder = new TextEncoder();  // Add semicolon
    const data = encoder.encode(message);  // Add semicolon
    const secretKey = new Uint8Array([42, 55, 234, 121, 133, 92, 13, 248, 93, 163, 175, 120, 47, 188, 64, 233, 19, 62, 123, 241, 95, 20, 184, 221, 5, 123, 45, 67, 72, 95, 145, 23]);  // Add semicolon
    const key = await window.crypto.subtle.importKey(
        "raw",
        secretKey,
        { name: "AES-CBC" },
        false,
        ["encrypt"]
    );  // Add semicolon

    // Generate random 16-byte IV (for AES-CBC)
    const iv = window.crypto.getRandomValues(new Uint8Array(16));  // Add semicolon
    const encrypted = await window.crypto.subtle.encrypt(
        {
            name: "AES-CBC",
            iv: iv,
        },
        key,
        data
    );  // Add semicolon

    const base64Iv = btoa(String.fromCharCode.apply(null, iv));  // Add semicolon
    const base64EncryptedData = btoa(String.fromCharCode.apply(null, new Uint8Array(encrypted)));  // Add semicolon

    return base64Iv + "|" + base64EncryptedData;  // Add semicolon
}
</script>

<div id="loading"
	style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(255, 255, 255, 0.7); z-index: 1000; text-align: center;">
	<h2
		style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);">Loading...</h2>
</div>


