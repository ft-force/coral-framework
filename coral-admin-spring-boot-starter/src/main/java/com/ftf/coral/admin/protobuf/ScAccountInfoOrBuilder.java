// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: scsession.proto

package com.ftf.coral.admin.protobuf;

public interface ScAccountInfoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:ftf.coral.admin.protobuf.ScAccountInfo)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.google.protobuf.Int32Value category = 1;</code>
   */
  boolean hasCategory();
  /**
   * <code>.google.protobuf.Int32Value category = 1;</code>
   */
  com.google.protobuf.Int32Value getCategory();
  /**
   * <code>.google.protobuf.Int32Value category = 1;</code>
   */
  com.google.protobuf.Int32ValueOrBuilder getCategoryOrBuilder();

  /**
   * <code>.google.protobuf.Int64Value accountId = 2;</code>
   */
  boolean hasAccountId();
  /**
   * <code>.google.protobuf.Int64Value accountId = 2;</code>
   */
  com.google.protobuf.Int64Value getAccountId();
  /**
   * <code>.google.protobuf.Int64Value accountId = 2;</code>
   */
  com.google.protobuf.Int64ValueOrBuilder getAccountIdOrBuilder();

  /**
   * <code>string username = 3;</code>
   */
  java.lang.String getUsername();
  /**
   * <code>string username = 3;</code>
   */
  com.google.protobuf.ByteString
      getUsernameBytes();

  /**
   * <code>repeated string roles = 4;</code>
   */
  java.util.List<java.lang.String>
      getRolesList();
  /**
   * <code>repeated string roles = 4;</code>
   */
  int getRolesCount();
  /**
   * <code>repeated string roles = 4;</code>
   */
  java.lang.String getRoles(int index);
  /**
   * <code>repeated string roles = 4;</code>
   */
  com.google.protobuf.ByteString
      getRolesBytes(int index);

  /**
   * <pre>
   * 登录时间
   * </pre>
   *
   * <code>.google.protobuf.Int64Value loginTime = 5;</code>
   */
  boolean hasLoginTime();
  /**
   * <pre>
   * 登录时间
   * </pre>
   *
   * <code>.google.protobuf.Int64Value loginTime = 5;</code>
   */
  com.google.protobuf.Int64Value getLoginTime();
  /**
   * <pre>
   * 登录时间
   * </pre>
   *
   * <code>.google.protobuf.Int64Value loginTime = 5;</code>
   */
  com.google.protobuf.Int64ValueOrBuilder getLoginTimeOrBuilder();
}