Spring Security stores your login in the HTTP session (backed by a session cookie like JSESSIONID).
      In Chrome, all tabs in the same browser profile share the same cookies for a given site.
      When you log in as a doctor in one tab, your session is tied to that doctor account.
      When you then log in as a patient in another tab (same browser), you overwrite the session cookie with the patient’s login.
      Now, when you go back to the doctor tab and try to do something (edit availability, cancel appointment, etc.), Spring Security sees your session as patient, not doctor →
      If you hit a /doctor/** URL, you don’t have ROLE_DOCTOR anymore → 403 Forbidden or Whitelabel error.
      If you hit a /patient/** URL while logged in as doctor, same problem.

In short: you can’t be logged in as two different users in the same browser session at the same time.

How to fix / work around it
Option 1 — Use different browsers
      Log in as doctor in Chrome.
      Log in as patient in Firefox / Edge / another browser.
      Each browser keeps its own cookies and session.
