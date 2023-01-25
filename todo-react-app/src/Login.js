import React from "react";
import {
  Container,
  Grid,
  Typography,
  TextField,
  Button,
  Alert,
  Snackbar,
  IconButton
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { Link } from "react-router-dom";
import { signin } from "./service/ApiService";

function Login() {

  const [open, setOpen] = React.useState(false);

  const handleClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }

    setOpen(false);
  };


  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.target);
    const username = data.get("username");
    const password = data.get("password");
    // ApiService의 signin 메서드를 사용 해 로그인.
    const returnPrms = signin({ username: username, password: password }); //promise값을 리턴받음
    // console.log("returnPrms",returnPrms);
    returnPrms.then((appData) => {
      // console.log("appData",appData);
      if(appData == 101){
        setOpen(true);
      }
    });    
  };

  const action = (
    <React.Fragment>
      {/* <Button color="secondary" size="small" onClick={handleClose}>
        UNDO
      </Button> */}
      <IconButton
        size="small"
        aria-label="close"
        color="inherit"
        onClick={handleClose}
      >
        <CloseIcon fontSize="small" />
      </IconButton>
    </React.Fragment>
  );

  return (
    <Container component="main" maxWidth="xs" style={{ marginTop: "8%" }}>
      <Grid container spacing={2}>
        <Grid item xs={12}>
          <Typography component="h1" variant="h5">
            로그인
          </Typography>
        </Grid>
      </Grid>
      <form noValidate onSubmit={handleSubmit}>
        {" "}
        {/* submit 버튼을 누르면 handleSubmit이 실행됨. */}
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <TextField
              variant="outlined"
              required
              fullWidth
              id="username"
              label="아이디"
              name="username"
              autoComplete="username"
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              variant="outlined"
              required
              fullWidth
              name="password"
              label="패스워드"
              type="password"
              id="password"
              autoComplete="current-password"
            />
          </Grid>
          <Grid item xs={12}>
            <Button type="submit" fullWidth variant="contained" color="primary">
              로그인
            </Button>
          </Grid>
          <Grid item>
            <Link to="/signup" variant="body2">
            계정이 없습니까? 여기서 가입 하세요.
            </Link>
          </Grid>
          <Snackbar
            open={open}
            autoHideDuration={3000}
            onClose={handleClose}
            message="Login Failed"
            action={action}
            onClick={handleClose}
          >
            {/* <Alert onClose={handleClose} severity="warning" sx={{ width: '100%' }}>
              Login Failed
            </Alert> */}
          </Snackbar>
        </Grid>
      </form>
    </Container>
  );
}

export default Login;
