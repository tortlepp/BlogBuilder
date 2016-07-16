<?php

if (isset($_GET["page"])) {
  switch ($_GET["page"]) {
$$URLS$$
    default: error();
  }
} else {
  error();
}

function redirect($url) {
  header("HTTP/1.1 301 Moved Permanently");
  header("Location: ".$url);
  header("Connection: close");
  die();
}

function error() {
  echo "Unknown ID";
  die();
}

?>