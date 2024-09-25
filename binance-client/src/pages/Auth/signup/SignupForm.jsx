/* eslint-disable no-unused-vars */
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from "@/components/ui/form";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { register } from "@/Redux/Auth/Action";
import SpinnerBackdrop from "@/components/custome/SpinnerBackdrop";

const formSchema = z.object({
  fullName: z.string().nonempty("Full name is required"),
  email: z.string().email("Please enter a valid email address").optional(),
  password: z
    .string()
    .min(8, "Password must be at least 8 characters long")
    .optional(),
  mobile: z
    .string()
    .min(10, "Phone number must be at least 10 characters long")
    .max(15, "Phone number must be at most 15 characters long")
    .optional(),
  address: z.string().nonempty("Address is required"),
  postCode: z
    .string()
    .max(10, "Postcode must be at most 10 characters long")
    .optional(),
  city: z.string().nonempty("City is required"),
  country: z.string().nonempty("Country is required"),
});

const SignupForm = () => {
  const {auth}=useSelector(store=>store)

  const navigate=useNavigate();
  const dispatch=useDispatch()
  const form = useForm({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: "",
      fullName: "",
      mobile: "",
      address: "",
      postCode:"",
      city: "",
      country: ""
    },
  });
  const onSubmit = (data) => {
    data.navigate=navigate
    dispatch(register(data))
    console.log("signup form", data);
  };
  return (
    
    <div className="space-y-0">
      <h1 className="text-center text-xl">Welcome to Binance</h1>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
          <FormField
            control={form.control}
            name="fullName"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    {...field}
                    type="text"
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="Enter your name" required
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    {...field}
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="Enter your email" required
                  />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    {...field}
                    type="password" 
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="Enter your password" required
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
           <FormField
            control={form.control}
            name="mobile"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    {...field}
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="Enter your phone" required
                  />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />
           <FormField
            control={form.control}
            name="address"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    {...field}
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="Enter your address" required
                  />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="city"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    {...field}
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="Enter your city" required
                  />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="postCode"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    {...field}
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="Enter your postcode" required
                  />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="country"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    {...field}
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="Enter your country" required
                  />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />
         {!auth.loading? <Button type="submit" className="w-full  py-5">
            Next
          </Button>:<SpinnerBackdrop show={true}/>}
        </form>
      </Form>
    </div>
  );
};

export default SignupForm;
