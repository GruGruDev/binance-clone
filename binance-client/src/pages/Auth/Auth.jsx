/* eslint-disable no-unused-vars */
import "./Auth.css";
import { Button } from "@/components/ui/button";

import SignupForm from "./signup/SignupForm";
import LoginForm from "./login/login";
import { useLocation, useNavigate } from "react-router-dom";
import { useState } from "react";
import ForgotPasswordForm from "./ForgotPassword";
import { useSelector } from "react-redux";
import { useToast } from "@/components/ui/use-toast";
import CustomeToast from "@/components/custome/CustomeToast";

const Auth = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { auth } = useSelector((store) => store);
  const { toast } = useToast();

  const [animate, setAnimate] = useState(false);

  const handleNavigation = (path) => {
    navigate(path);
  };

  const [showToast, setShowToast] = useState(false);

  const handleShowToast = () => {
    setShowToast(true);
  };

  return (
    <div className={`authContainer h-screen relative`}>
      <div className="absolute top-0 right-0 left-0 bottom-0 bg-[#030712] bg-opacity-50"></div>
      <div
        className={`bgBlure absolute top-1/2 left-1/2 transform -translate-x-1/2  -translate-y-1/2 box flex flex-col justify-center items-center  h-[35rem] w-[30rem]  rounded-md z-50 bg-black bg-opacity-50 shadow-2xl shadow-white`}
      >
        <CustomeToast show={auth.error} message={auth.error?.error} />
        <h1 className="text-5xl font-bold pb-9 text-yellow-500">BINANCE</h1>
        {location.pathname == "/signup" ? (
          <section
            className={`w-full login  ${animate ? "slide-down" : "slide-up"}`}
          >
            <div className={`  loginBox  w-full px-10 space-y-5 `}>
              <SignupForm />

              {location.pathname == "/signup" ? (
                <div className="flex items-center justify-center">
                  <span> {"Already have an account?"} </span>
                  <Button
                    onClick={() => handleNavigation("/signin")}
                    variant="ghost" className='font-bold'
                  >
                    Log in
                  </Button>
                </div>
              ) : (
                <div className="flex items-center justify-center">
                  <span>Create a Binance Account</span>
                  <Button
                    onClick={() => handleNavigation("/signup")}
                    variant="ghost" className='font-bold'
                  >
                    Sign up
                  </Button>
                </div>
              )}
            </div>
          </section>
        ) : location.pathname == "/forgot-password" ? (
          <section className="p-5 w-full">
            <ForgotPasswordForm />
            <div className="flex items-center justify-center mt-5">
              <span className='text-yellow-500'>Sign up as entity? </span>
              <Button onClick={() => navigate("/signin")} variant="ghost" className='font-bold'>
                Log in
              </Button>
            </div>
          </section>
        ) : (
          <>
            {
              <section className={`w-full login`}>
                <div className={`  loginBox  w-full px-10 space-y-5 `}>
                  <LoginForm />

                  <div className="flex items-center justify-center">
                    <span>Don't have an account?</span>
                    <Button
                      onClick={() => handleNavigation("/signup")}
                      variant="ghost" className='font-bold text-yellow-500'
                    >
                      Sign up
                    </Button>
                  </div>
                  <div className="">
                    <Button
                      onClick={() => navigate("/forgot-password")}
                      variant="outline"
                      className="w-full py-5"
                    >
                      <b className="font-bold text-yellow-500">Forgot Password</b>
                    </Button>
                  </div>
                </div>
              </section>
            }
          </>
        )}
      </div>
    </div>
  );
};

export default Auth;
